package com.pbkj.crius.redisjob.redisinaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

import java.util.*;

/**
 * @author gzq
 * 用redis实现文章排行网站
 */
public class Chapter01 {

    private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    private static final int VOTE_SCORE = 432;
    private static final int ARTICLES_PER_PAGE = 25;

    private static final String ARTICLE = "article:";
    private static final String SCORE = "score:";
    private static final String VOTES = "votes:";
    private static final String NEGATIVE_VOTE = "negativeVotes:";
    private static final String TIME = "time:";
    private static final String GROUP = "group:";



    private void postArticle(Jedis conn,String poster,String title,String link){
        Long articleId = conn.incr(ARTICLE);
        //每篇文章的投票记录
        String votes = VOTES+articleId;
        conn.sadd(votes,poster);
        conn.expire(votes,ONE_WEEK_IN_SECONDS);
        //组合文章
        String article = ARTICLE + articleId;
        long nowTime = System.currentTimeMillis() / 1000;
        HashMap<String, String> articleData = Maps.newHashMapWithExpectedSize(5);
        articleData.put("title",title);
        articleData.put("link",link);
        articleData.put("poster",poster);
        articleData.put("now",String.valueOf(nowTime));
        articleData.put("votes","1");
        conn.hmset(article,articleData);
        //将创建时间保存到，有序集合中
        conn.zadd(TIME,nowTime,article);
        //将文章分数保存到，有序集合中
        conn.zadd(SCORE,nowTime+VOTE_SCORE,article);
    }

    private void articleVote(Jedis conn,String user,String article,String votesTypeKey){
        //发布时间大于一周的不能投票
        long cutoff= System.currentTimeMillis() / 1000 - ONE_WEEK_IN_SECONDS;
        if(conn.zscore(TIME,article)< cutoff){
            return;
        }
        String articleId = article.substring(article.indexOf(":") + 1);
        if(VOTES.equals(votesTypeKey)){
            if(conn.sadd(VOTES+ articleId,user)==1){
                conn.hincrBy(article,"votes",1L);
                conn.zincrby(SCORE,VOTE_SCORE,article);
            }
        }else {
            if(conn.sadd(NEGATIVE_VOTE+ articleId,user)==1){
                conn.hincrBy(article,"votes",-1L);
                conn.zincrby(SCORE,-VOTE_SCORE,article);
            }
        }
    }

    /**
     * 选票互换功能
     * @param conn
     * @param article
     */
    private void exchangeVote(Jedis conn,String article){
        String articleId = article.substring(article.indexOf(":") + 1);
        String votesArticle = VOTES + articleId;
        String negativeVoteArticle = NEGATIVE_VOTE + articleId;
        Set<String> voteMembers = conn.smembers(votesArticle);
        Set<String> negativeVoteMembers = conn.smembers(negativeVoteArticle);
        voteMembers.forEach(e-> conn.smove(votesArticle,negativeVoteArticle,e));
        negativeVoteMembers.forEach(e-> conn.smove(negativeVoteArticle,votesArticle,e));
    }

    private List<Map<String,String>> getArticles(Jedis conn,int page,String order){
        int start = (page - 1) * ARTICLES_PER_PAGE;
        int end = start + ARTICLES_PER_PAGE - 1;

        List<Map<String,String>> articles = Lists.newArrayListWithCapacity(25);
        Set<String> ids = conn.zrevrange(order, start, end);
        ids.forEach(id->{
            Map<String, String> articleData = conn.hgetAll(id);
            articles.add(articleData);
        });
        return articles;
    }

    private void addGroup(Jedis conn,String articleId,String[] toAdd){
        String article = ARTICLE + articleId;
        for (String group:toAdd) {
            conn.sadd(GROUP+group,article);
        }
    }
    /**
     * 获取组中评分最高的文章，思路就是将组集合和评分集合做交集
     */
    private List<Map<String,String>> getGroupArticles(Jedis conn,String group,int page,String order){
        String key = order + group;
        if(!conn.exists(key)){
            ZParams zParams = new ZParams().aggregate(ZParams.Aggregate.MAX);
            conn.zinterstore(key,zParams,GROUP+group,order);
            conn.expire(key,60);
        }
        return getArticles(conn,page,key);
    }
    public List<Map<String,String>> getGroupArticles(Jedis conn,String group,int page){
        return getGroupArticles(conn,group,page,SCORE);
    }
}
