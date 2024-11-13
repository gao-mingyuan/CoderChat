//package com.gmy.coder.chat.websocket.websocket;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpServerCodec;
//import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import io.netty.handler.stream.ChunkedWriteHandler;
//import io.netty.handler.timeout.IdleStateHandler;
//import io.netty.util.NettyRuntime;
//import io.netty.util.concurrent.Future;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//
//@Slf4j
//@Configuration
//public class NettyWebSocketServer {
//    public static final int WEB_SOCKET_PORT = 8090;
//    public static final NettyWebSocketServerHandler NETTY_WEB_SOCKET_SERVER_HANDLER = new NettyWebSocketServerHandler();
//    // 创建线程池执行器
//    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//    private EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());
//
//    /**
//     * 启动 ws server
//     *
//     * @return
//     * @throws InterruptedException
//     */
//    @PostConstruct
//    public void start() throws InterruptedException {
//        run();
//    }
//
//    /**
//     * 销毁
//     */
//    @PreDestroy
//    public void destroy() {
//        Future<?> future = bossGroup.shutdownGracefully();
//        Future<?> future1 = workerGroup.shutdownGracefully();
//        future.syncUninterruptibly();
//        future1.syncUninterruptibly();
//        log.info("关闭 ws server 成功");
//    }
//
//    public void run() throws InterruptedException {
//        // 服务器启动引导对象
//        ServerBootstrap serverBootstrap = new ServerBootstrap();
//        serverBootstrap.group(bossGroup, workerGroup)
//                //指定服务器的通道类型为NioServerSocketChannel，它表示使用NIO传输。
//                .channel(NioServerSocketChannel.class)
//                //设置服务器的socket参数，指定等待接受的连接的队列的最大长度。
//                .option(ChannelOption.SO_BACKLOG, 128)
//                //设置服务器的socket参数，保持连接活跃，即长连接。
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                //为bossGroup添加一个日志处理器LoggingHandler，用于记录日志信息，日志级别为INFO。
//                .handler(new LoggingHandler(LogLevel.INFO))
//                //设置每个连接的处理器，当一个新的连接被接受时，会创建一个新的SocketChannel对象，并绑定相应的处理器。
//                .childHandler(
//                        //ChannelInitializer是一个特殊的处理器，用于帮助用户配置新的Channel。
//                        new ChannelInitializer<SocketChannel>() {
//                            //对新连接的Channel进行初始化，添加一系列的处理器到ChannelPipeline中，这些处理器按顺序处理数据流。
//                            @Override
//                            protected void initChannel(SocketChannel socketChannel) throws Exception {
//                                //ChannelPipeline是一个处理输入和输出数据的链，它包含了一系列的ChannelHandler。
//                                ChannelPipeline pipeline = socketChannel.pipeline();
//                                //30秒客户端没有向服务器发送心跳则关闭连接
//                                pipeline.addLast(new IdleStateHandler(30, 0, 0));
//                                //因为使用http协议，所以需要使用http的编码器，解码器
//                                pipeline.addLast(new HttpServerCodec());
//                                //用于处理大数据流，以块方式写。
//                                pipeline.addLast(new ChunkedWriteHandler());
//                                /**
//                                 * 说明：
//                                 *  1. http数据在传输过程中是分段的，HttpObjectAggregator可以把多个段聚合起来；
//                                 *  2. 这就是为什么当浏览器发送大量数据时，就会发出多次 http请求的原因
//                                 */
//                                pipeline.addLast(new HttpObjectAggregator(8192));
//                                //保存用户ip
//                                pipeline.addLast(new HttpHeadersHandler());
//                                /**
//                                 * 说明：
//                                 *  1. 对于 WebSocket，它的数据是以帧frame 的形式传递的；
//                                 *  2. 可以看到 WebSocketFrame 下面有6个子类
//                                 *  3. 浏览器发送请求时： ws://localhost:7000/hello 表示请求的uri
//                                 *  4. WebSocketServerProtocolHandler 核心功能是把 http协议升级为 ws 协议，保持长连接；
//                                 *      是通过一个状态码 101 来切换的
//                                 */
//                                pipeline.addLast(new WebSocketServerProtocolHandler("/"));
//                                // 自定义handler ，处理业务逻辑
//                                pipeline.addLast(NETTY_WEB_SOCKET_SERVER_HANDLER);
//                            }
//                        });
//        // 启动服务器，监听端口，阻塞直到启动成功
//        serverBootstrap.bind(WEB_SOCKET_PORT).sync();
//    }
//
//}
