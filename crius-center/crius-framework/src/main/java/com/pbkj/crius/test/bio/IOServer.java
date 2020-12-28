package com.pbkj.crius.test.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IOServer {

  public static void main(String[] args) throws IOException {
    // TODO 服务端处理客户端连接请求
    ServerSocket serverSocket = new ServerSocket(3333);
    // 接收到客户端连接请求之后为每个客户端创建一个新的线程进行链路处理int i = 1;
    new Thread(() -> {
      while (true) {
        try {
          // 阻塞方法获取新的连接
            System.out.println("==");
            Socket socket = serverSocket.accept();
          // 每一个新的连接都创建一个线程，负责读取数据
          new Thread(() -> {
              System.out.println("负责读取数据开启的的线程开始");
            try {
              int len;
              byte[] data = new byte[1024];
              InputStream inputStream = socket.getInputStream();
              // 按字节流方式读取数据
                while ((len = inputStream.read(data)) != -1) {
                System.out.println(new String(data, 0, len));
              }
                System.out.println("负责读取数据开启的的线程结束");
            } catch (IOException e) {
            }
          }).start();

        } catch (IOException e) {
        }

      }
    }).start();

  }

}


