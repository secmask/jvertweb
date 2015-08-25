package org.sm.jvertweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;

public class JVertxWebMain {
	static String data = null;
	static{
		try {
			data = new String(Files.readAllBytes(Paths.get("resp.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static class ApplicationHttpVert extends AbstractVerticle {
		
		private HttpServer server;
		public ApplicationHttpVert() {
			
		}
		@Override
		public void init(Vertx vertx, Context context) {
			super.init(vertx, context);
		}
		@Override
		public void start(Future<Void> statFuture) throws Exception {
			HttpServerOptions opt = new HttpServerOptions();
			opt.setCompressionSupported(true);
			server = getVertx().createHttpServer(opt);			
			server.requestHandler(context->{
				context.response().putHeader("Content-Type", "text/plain; charset=UTF-8");
				context.response().end(data);
			});
			server.listen(8080,f->{
				statFuture.complete();
			});
		}
		@Override
		public void stop(Future<Void> stopFuture) throws Exception {
			if(this.server!=null){
				this.server.close(f->{
					stopFuture.complete();
				});
			}else{
				stopFuture.complete();
			}
		}
	}

	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();		
		vertx.deployVerticle(ApplicationHttpVert.class.getName(),new DeploymentOptions().setInstances(8));
	}

}
