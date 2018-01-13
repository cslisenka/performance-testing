package com.example.demo;

import com.example.demo.dto.*;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import de.codecentric.boot.admin.config.EnableAdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@EnableAdminServer
@SpringBootApplication
@Configuration
@RestController
public class DemoChatApplication {

	private Logger log = LoggerFactory.getLogger(DemoChatApplication.class);

	@Autowired
	private JdbcTemplate jdbc;

	public static void main(String[] args) {
		SpringApplication.run(DemoChatApplication.class, args);
	}

    @GetMapping("/dummy")
    public Response dummyPost(@RequestParam String request, @RequestParam int delay) throws InterruptedException {
        log.debug("/dummy {}", request);
        if (delay > 0) {
            Thread.sleep(delay);
        }

        return new Response("Response: " + request);
    }

	@PostMapping("/dummy")
	public Response dummy(@RequestBody String request, @RequestParam int delay, @RequestParam int responseSize) throws InterruptedException {
        log.debug("/dummy {}", request);
        if (delay > 0) {
            Thread.sleep(delay);
        }

        StringBuilder response = new StringBuilder();
        for (int i = 0; i < responseSize; i++) {
        	response.append(request);
		}

        return new Response("Response: " + response.toString());
    }

	@PostMapping("/user/add")
	@ResponseBody
	public Response addUser(@RequestBody UserAddRequest request) throws InterruptedException {
		log.info("/user/add {}", request);
		if (request.getDelay() > 0) {
			Thread.sleep(request.getDelay());
		}

		jdbc.update("INSERT INTO users (login) VALUES (?)", request.getLogin());

		return new Response("User created: " + request.getLogin());
	}

	@PostMapping("/room/add")
	@ResponseBody
	public Response addRoom(@RequestBody RoomAddRequest request) throws InterruptedException {
		log.info("/room/add {}", request);
		if (request.getDelay() > 0) {
			Thread.sleep(request.getDelay());
		}

		jdbc.update("INSERT INTO rooms (name) VALUES (?)", request.getName());

		return new Response("Room created: " + request.getName());
	}

	@PostMapping("/message/add")
	@ResponseBody
	public Response addMessage(@RequestBody MessageAddRequest request) throws InterruptedException {
		log.info("/message/add {}", request);
		if (request.getDelay() > 0) {
			Thread.sleep(request.getDelay());
		}

		int userId = jdbc.queryForObject("SELECT id FROM users WHERE login=?", Integer.class, request.getUser());
		int roomId = jdbc.queryForObject("SELECT id FROM rooms WHERE name=?", Integer.class, request.getRoom());

		jdbc.update("INSERT INTO messages (user_id, room_id, text, time) VALUES (?, ?, ?, now())", userId, roomId, request.getMessage());

		return new Response("Message created: " + request.getMessage());
	}

	@GetMapping("/message/get")
	@ResponseBody
	public List<Message> getMessages(@RequestParam String room, @RequestParam int limit, @RequestParam int delay) throws InterruptedException {
		log.info("/message/get room={} limit={} delay={}", room, limit, delay);
		if (delay > 0) {
			Thread.sleep(delay);
		}

		List<Message> result = jdbc.query("SELECT m.text AS text, m.time AS time, r.name AS room, u.login AS user FROM messages m " +
			"INNER JOIN rooms r ON m.room_id = r.id " +
			"INNER JOIN users u ON m.user_id = u.id " +
			"WHERE r.name=? " +
			"ORDER BY m.time DESC " +
			"LIMIT ?", new BeanPropertyRowMapper(Message.class), room, limit);

		return result;
	}

	@Bean
	public JdbcTemplate jdbc() {
		return new JdbcTemplate(ds());
	}

	@Bean
	public DataSource ds() {
		MysqlDataSource ds = new MysqlDataSource();
		ds.setCreateDatabaseIfNotExist(true);
		ds.setDatabaseName("performance");
		ds.setUser("root");
		ds.setPassword("root");
		return ds;
	}
}