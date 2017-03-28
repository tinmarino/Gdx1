package com.mygdx.gdx1.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

	// TODO myExtIp
	// myIntIp
	// give callback to listening 
	// make library gitted and stuff, clean,independant from test? 
	// Easy discovery cyling around 192.168.0 or kryonet
public class NetInput implements Screen {
	int PORT = 9012; // one app listen on port 
	enum TYPE {
		SERVER, CLIENT
	}

	private Stage stage;
	private Skin skin;
	private Label labelDetails, labelMessage;
	private TextButton button;
	private TextArea textIPAddress, textMessage;


	private interface RunnableString{
		public void run(String string);
	}
	private RunnableString runnableString;


	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);


        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        labelDetails = new Label(myIp(), skin);
        labelMessage = new Label("Hello world",skin);
        button = new TextButton("Send message",skin);
        textIPAddress = new TextArea("",skin);
        textMessage = new TextArea("",skin);


        VerticalGroup vg = new VerticalGroup().space(3).pad(5).fill();
		vg.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		vg.addActor(labelDetails);
		vg.addActor(labelMessage);
		vg.addActor(button);
		vg.addActor(textIPAddress);
		vg.addActor(textMessage);



		stage.addActor(vg);



		// MOUTH
        button.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
				sendMessage(textMessage.getText(), textIPAddress.getText(), PORT);
			}
        });

		// EAR 
		runnableString = new RunnableString(){
			@Override
			public void run(String string){
				labelMessage.setText(string);
			}
		};
		startListening(runnableString);
	}




	@Override
	public void render(float arg0) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
	}

	public static void sendMessage(String textToSend, String ipAddress, int port){
		if(textToSend.length() == 0){
			textToSend = "Doesn't say much but likes clicking buttons\n";
		}

		textToSend += "\n";
		try {
		SocketHints socketHints = new SocketHints();
		socketHints.connectTimeout = 4000; // 4 sec
		Socket socket = Gdx.net.newClientSocket(Protocol.TCP, ipAddress, port, socketHints);
			socket.getOutputStream().write(textToSend.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startListening(RunnableString runnableString){
        new Thread(new Runnable(){

            @Override
            public void run() {
				try{
					ServerSocketHints serverSocketHint = new ServerSocketHints();
					serverSocketHint.acceptTimeout = 0;
					ServerSocket serverSocket = Gdx.net.newServerSocket(Protocol.TCP, PORT, serverSocketHint);
					
					while(true){
						Socket socket = serverSocket.accept(null);
						BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
						try {
							labelMessage.setText(buffer.readLine());    
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
            }
        }).start(); // And, start the thread running
	}



	public static void Log(String string){
		Gdx.app.log("Gdx1 log:", string);
	}

	public static String myIp(){
		String res = "";
        try {
			res = "";
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface ni : Collections.list(interfaces)){
                for(InetAddress address : Collections.list(ni.getInetAddresses()))
                {
                    if(address instanceof Inet4Address){
						res += ni.getName() + ": ";
						res += address.getHostAddress() + "\n";
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
			res += "ERROR in get my ip :" + e.toString();
        }
		return res;
	}


	@Override
	public void dispose() { }

	@Override
	public void hide() { }

	@Override
	public void pause() { }

	@Override
	public void resize(int arg0, int arg1) { }

	@Override
	public void resume() { }


}
