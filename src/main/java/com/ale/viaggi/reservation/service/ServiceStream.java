package com.ale.viaggi.reservation.service;

public interface ServiceStream extends Service {

	void start(String kafkaServers);

	void stop();

}
