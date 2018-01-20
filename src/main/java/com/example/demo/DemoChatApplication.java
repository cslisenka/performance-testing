package com.example.demo;

import com.example.demo.dto.*;
import de.codecentric.boot.admin.config.EnableAdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.util.List;

@EnableAdminServer
@SpringBootApplication
@Configuration
@RestController
public class DemoChatApplication {

	private Logger log = LoggerFactory.getLogger(DemoChatApplication.class);

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private HttpSession session;

	public static void main(String[] args) {
		SpringApplication.run(DemoChatApplication.class, args);
	}

	@GetMapping("/login")
	public Response login() {
	    session.setAttribute("login", "true");
	    return new Response("OK (sessionId=" + session.getId() + ")");
    }

    private void handleAuthentication() {
        if (session.getAttribute("login") == null) {
            throw new RuntimeException("Not authenticated");
        }
    }

    private void handleDelay(int delay) throws InterruptedException {
        if (delay > 0) {
            Thread.sleep(delay);
        }
    }

    @GetMapping("/dummy")
    public Response dummyGet(@RequestParam String request, @RequestParam int delay) throws InterruptedException {
        log.debug("/dummy {}", request);
        handleDelay(delay);
        return new Response("Response: " + request);// + " (" + session.getId() + ", " + session.getCreationTime() + ")");
    }

	@PostMapping("/dummy")
	public Response dummyPost(@RequestBody String request, @RequestParam int delay, @RequestParam int responseSize) throws InterruptedException {
        log.debug("/dummy {}", request);
        handleDelay(delay);

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
		handleDelay(request.getDelay());

		// Experiment with forced creation of prepared statement
//		jdbc.update("INSERT INTO users (login) VALUES (?)", request.getLogin());
		jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (login) VALUES (?)");
            ps.setString(1, request.getLogin());
            return ps;
        });

		return new Response("User created: " + request.getLogin());
	}

	@GetMapping("/user")
    @ResponseBody
	public List<User> getUsers(int limit) {
//	    handleAuthentication();

        log.info("/user");
        return jdbc.query("SELECT *  FROM users " +
            "LIMIT ?", new BeanPropertyRowMapper(User.class), limit);
    }

	@PostMapping("/room/add")
	@ResponseBody
	public Response addRoom(@RequestBody RoomAddRequest request) throws InterruptedException {
        handleAuthentication();

		log.info("/room/add {}", request);
		jdbc.update("INSERT INTO rooms (name) VALUES (?)", request.getName());
		return new Response("Room created: " + request.getName());
	}

    @GetMapping("/room")
    @ResponseBody
    public List<Room> getRooms(int limit) {
	    handleAuthentication();

        log.info("/room");
        return jdbc.query("SELECT *  FROM rooms " +
                "LIMIT ?", new BeanPropertyRowMapper(Room.class), limit);
    }

    @GetMapping("/room/popular")
    @ResponseBody
    public List<Room> getMostPopularRooms(int limit) {
        handleAuthentication();

        log.info("/room/popular");
        // TODO change SQL
        return jdbc.query("SELECT *  FROM rooms " +
                "LIMIT ?", new BeanPropertyRowMapper(Room.class), limit);
    }

    @GetMapping("/room/recent")
    @ResponseBody
    public List<Room> getRecentlyCommentedRooms(int limit) {
        handleAuthentication();

        log.info("/room/recent");
        // TODO change SQL
        return jdbc.query("SELECT *  FROM rooms " +
                "LIMIT ?", new BeanPropertyRowMapper(Room.class), limit);
    }

	@PostMapping("/message/add")
	@ResponseBody
	public Response addMessage(@RequestBody MessageAddRequest request) throws InterruptedException {
	    handleAuthentication();

		log.info("/message/add {}", request);
		handleDelay(request.getDelay());
		// TODO try using cache
		int userId = jdbc.queryForObject("SELECT id FROM users WHERE login=?", Integer.class, request.getUser());
		int roomId = jdbc.queryForObject("SELECT id FROM rooms WHERE name=?", Integer.class, request.getRoom());

		jdbc.update("INSERT INTO messages (user_id, room_id, text, time) VALUES (?, ?, ?, now())", userId, roomId, request.getMessage());
		return new Response("Message created: " + request.getMessage());
	}

	@GetMapping("/message")
	@ResponseBody
	public List<Message> getMessages(@RequestParam String room, @RequestParam int limit) throws InterruptedException {
	    handleAuthentication();

		log.info("/message/get room={} limit={}", room, limit);

		List<Message> result = jdbc.query("SELECT m.text AS text, m.time AS time, r.name AS room, u.login AS user FROM messages m " +
			"LEFT JOIN rooms r ON m.room_id = r.id " +
			"LEFT JOIN users u ON m.user_id = u.id " +
			"WHERE r.name=? " +
			"ORDER BY m.time DESC " +
			"LIMIT ?", new BeanPropertyRowMapper(Message.class), room, limit);

		return result;
	}

	@GetMapping("/message/last/get")
	@ResponseBody
	public List<Message> getLastMessages(@RequestParam int limit) throws InterruptedException {
        handleAuthentication();

		log.info("/message/get limit={}", limit);
		List<Message> result = jdbc.query("SELECT m.text AS text, m.time AS time, r.name AS room, u.login AS user FROM messages m " +
				"LEFT JOIN rooms r ON m.room_id = r.id " +
				"LEFT JOIN users u ON m.user_id = u.id " +
				"ORDER BY m.time DESC " +
				"LIMIT ?", new BeanPropertyRowMapper(Message.class), limit);
		return result;
	}
}