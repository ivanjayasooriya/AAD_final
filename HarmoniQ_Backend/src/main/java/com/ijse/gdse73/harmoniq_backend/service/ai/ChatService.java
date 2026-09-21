//package com.ijse.gdse73.harmoniq_backend.service.ai;
//
//import com.ijse.gdse73.harmoniq_backend.entity.*;
//import com.ijse.gdse73.harmoniq_backend.repo.FollowedArtistRepo;
//import com.ijse.gdse73.harmoniq_backend.repo.MusicRepo;
//import com.ijse.gdse73.harmoniq_backend.repo.UserRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ChatService {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final MusicRepo musicRepo;
//    private final FollowedArtistRepo followedArtistRepo;
//    private final UserRepo userRepo;
//
//    private String contextData;
//
//    public void setContext(Long userId) {
//        this.contextData = buildUserContext(userId);
//    }
//
//    public String askAI(String msg) {
//        String url = "http://localhost:11434/api/generate";
//
//        String prompt = String.format("""
//    ### SYSTEM DATABASE
//    %s
//
//    ### MANDATORY RULES:
//    1. INTENT RECOGNITION:
//       - If the user provides a greeting (e.g., "Hi", "Hello"), respond with a friendly greeting and ask how you can help.
//       - If the user says "Thank you", respond politely.
//       - If the user asks a general question about the system (e.g., "What can you do?"), explain that you provide song recommendations from the database.
//
//    2. DATA LIMITATION:
//       - ONLY recommend or confirm songs listed in the 'AVAILABLE SONGS' section above.
//       - DO NOT use internal knowledge to suggest real-world songs not present in the list.
//
//    3. SEARCH/AVAILABILITY REQUESTS:
//       - If the user asks "Is there a song called X?", check the list.
//       - If it exists, respond: "Yes, 'X' is available. Would you like me to play it or do you have another request?"
//       - If it does NOT exist, respond: "No, 'X' is not in our database. Do you have any other request?"
//
//    4. RECOMMENDATION REQUESTS:
//       - If the user specifically asks for a recommendation or a genre, use the format: "I recommend you listen to 'Song Title' by 'Artist Name'".
//       - If no matches are found for their request, respond exactly: "No recommendations available".
//
//    5. SONG PLAY REQUESTS:
//       - If the user wants to play a song, you MUST find the Song ID in the list above.
//       - Respond with this exact tag: [ACTION:PLAY_SONG(id_number)]
//       - If the song isn't in the list, say "I couldn't find that song."
//
//    6. CREATE NEW PLAYLIST REQUESTS:
//       - If the user wants to create a new playlist, you MUST check the user Playlist list for duplicates.
//       - If there are no duplicates, respond with this exact tag: [ACTION:CREATE_PLAYLIST(playlist_name)]
//       - If there are duplicates, say "You already have a playlist with that name."
//
//    7. CREATE NEW PLAYLIST WITH SONGS REQUESTS:
//        - If the user asks to create a new playlist with specific songs (by genre, artist, etc.), you MUST:
//
//        i) Check the [AVAILABLE SONGS] list.
//        ii) Select exactly 3 songs that MATCH the requested genre/artist.
//        iii) ONLY select songs from the provided list. DO NOT make up songs.
//        iv) Extract their IDs.
//
//        - Then respond ONLY with this exact format(don't add spaces around playlist_name or id1,id2,id3. follow the format exactly):
//        [ACTION:CREATE_PLAYLIST_WITH_SONGS(playlist_name|id1,id2,id3)]
//
//        - Example:
//        [ACTION:CREATE_PLAYLIST_WITH_SONGS(Pop Songs|12,45,78)]
//
//        - If fewer than 3 matching songs are found:
//        Respond: "Not enough songs found to create this playlist."
//
//        - If a playlist with the same name already exists:
//        Respond: "You already have a playlist with that name."
//
//    8. CONCISENESS: Keep all responses brief and focused on the user's specific input.
//
//    ### USER REQUEST:
//    %s
//
//    ### ASSISTANT RESPONSE:
//    """, contextData, msg);
//
//        Map<String, Object> request = new HashMap<>();
//        request.put("model", "llama3");
//        request.put("prompt", prompt);
//        request.put("stream", false);
//
//        // Crucial: Set temperature to 0 to stop hallucinations
//        Map<String, Object> options = new HashMap<>();
//        options.put("temperature", 0.0);
//        request.put("options", options);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
//            if (response.getBody() != null && response.getBody().containsKey("response")) {
//                return response.getBody().get("response").toString().trim();
//            }
//            return "AI Error: Empty response";
//        } catch (Exception e) {
//            return "Error connecting to AI service: " + e.getMessage();
//        }
//    }
//
//    public String buildUserContext(Long userId) {
//        User user = userRepo.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        List<Music> allSongs = musicRepo.findAll();
//        List<FollowedArtist> followedArtists = followedArtistRepo.findAllByUser(user);
//        List<Music> userLikedSongs = user.getLikedSongs().stream().map(LikedSong::getMusic).toList();
//        List<Music> userRecentSongs = user.getRecentSongs().stream().map(RecentSong::getMusic).toList();
//        List<Playlist> userPlaylists = user.getPlaylists();
//
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("\n[AVAILABLE SONGS]\n");
//        for (Music m : allSongs) {
//            sb.append(String.format("- Id: '%d' |Title: '%s' | Artist: '%s' | Genre: '%s'\n",
//                    m.getId(), m.getMusicTitle(), m.getArtist().getName(), m.getGenre().getName()));
//        }
//
//        sb.append("\n[USER PREFERENCES]\n");
//        sb.append("- Followed Artists: ").append(
//                followedArtists.stream().map(a -> a.getArtist().getName()).collect(Collectors.joining(", "))
//        ).append("\n");
//
//        sb.append("- Liked Songs: ").append(
//                userLikedSongs.stream().map(Music::getMusicTitle).collect(Collectors.joining(", "))
//        ).append("\n");
//
//        sb.append("- Recently Played: ").append(
//                userRecentSongs.stream().map(Music::getMusicTitle).collect(Collectors.joining(", "))
//        ).append("\n");
//
//        sb.append("\n[USER PLAYLISTS]\n");
//        if (userPlaylists.isEmpty()) {
//            sb.append("- Playlists: None\n");
//        } else {
//            sb.append("- Playlists: ").append(
//                    userPlaylists.stream().map(Playlist::getPlaylistName).collect(Collectors.joining(", "))
//            ).append("\n");
//        }
//
//        return sb.toString();
//    }
//}

package com.ijse.gdse73.harmoniq_backend.service.ai;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Part;
import com.ijse.gdse73.harmoniq_backend.entity.*;
import com.ijse.gdse73.harmoniq_backend.repo.FollowedArtistRepo;
import com.ijse.gdse73.harmoniq_backend.repo.MusicRepo;
import com.ijse.gdse73.harmoniq_backend.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MusicRepo musicRepo;
    private final FollowedArtistRepo followedArtistRepo;
    private final UserRepo userRepo;

    @Value("${google.ai.api-key}")
    private String apiKey;

    private String contextData;

    public void setContext(Long userId) {
        this.contextData = buildUserContext(userId);
    }

    public String askAI(String msg) {
        String systemInstruction = """
            ### SYSTEM DATABASE
            %s
            
            ### MANDATORY RULES:
            1. INTENT RECOGNITION: 
               - If the user provides a greeting (e.g., "Hi", "Hello"), respond with a friendly greeting and ask how you can help.
               - If the user says "Thank you", respond politely.
               - If the user asks a general question about the system (e.g., "What can you do?"), explain that you provide song recommendations from the database.
               
            2. DATA LIMITATION:
               - ONLY recommend or confirm songs listed in the 'AVAILABLE SONGS' section above.
               - DO NOT use internal knowledge to suggest real-world songs not present in the list.
               
            3. SEARCH/AVAILABILITY REQUESTS:
               - If the user asks "Is there a song called X?", check the list. 
               - If it exists, respond: "Yes, 'X' is available. Would you like me to play it or do you have another request?"
               - If it does NOT exist, respond: "No, 'X' is not in our database. Do you have any other request?"
               
            4. RECOMMENDATION REQUESTS:
               - If the user specifically asks for a recommendation or a genre, use the format: "I recommend you listen to 'Song Title' by 'Artist Name'".
               - If no matches are found for their request, respond exactly: "No recommendations available".

            5. SONG PLAY REQUESTS:
               - If the user wants to play a song, you MUST find the Song ID in the list above.
               - Respond with this exact tag: [ACTION:PLAY_SONG(id_number)]
               - If the song isn't in the list, say "I couldn't find that song."
               
            6. CREATE NEW PLAYLIST REQUESTS:
               - If the user wants to create a new playlist, you MUST check the user Playlist list for duplicates.
               - If there are no duplicates, respond with this exact tag: [ACTION:CREATE_PLAYLIST(playlist_name)]
               - If there are duplicates, say "You already have a playlist with that name."
               
            7. CREATE NEW PLAYLIST WITH SONGS REQUESTS:
                - If the user asks to create a new playlist with specific songs (by genre, artist, etc.), you MUST:
                
                i) Check the [AVAILABLE SONGS] list.
                ii) Select exactly 3 songs that MATCH the requested genre/artist.
                iii) ONLY select songs from the provided list. DO NOT make up songs.
                iv) Extract their IDs.
                
                - Then respond ONLY with this exact format(don't add spaces around playlist_name or id1,id2,id3. follow the format exactly):
                [ACTION:CREATE_PLAYLIST_WITH_SONGS(playlist_name|id1,id2,id3)]
                
                - Example:
                [ACTION:CREATE_PLAYLIST_WITH_SONGS(Pop Songs|12,45,78)]
                
                - If fewer than 3 matching songs are found:
                Respond: "Not enough songs found to create this playlist."
                
                - If a playlist with the same name already exists:
                Respond: "You already have a playlist with that name."
               
            8. CONCISENESS: Keep all responses brief and focused on the user's specific input.
            """.formatted(contextData);

        try (Client client = Client.builder().apiKey(apiKey).build()) {

            // Set temperature to 0.0 to prevent hallucinations
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .systemInstruction(Content.fromParts(Part.fromText(systemInstruction)))
                    .temperature(0.0f)
                    .build();

            // Call Gemini 2.5 Flash model
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    msg,
                    config
            );

            if (response.text() != null && !response.text().trim().isEmpty()) {
                return response.text().trim();
            }
            return "AI Error: Empty response";

        } catch (Exception e) {
            return "Error connecting to Gemini AI service: " + e.getMessage();
        }
    }

    public String buildUserContext(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Music> allSongs = musicRepo.findAll();
        List<FollowedArtist> followedArtists = followedArtistRepo.findAllByUser(user);
        List<Music> userLikedSongs = user.getLikedSongs().stream().map(LikedSong::getMusic).toList();
        List<Music> userRecentSongs = user.getRecentSongs().stream().map(RecentSong::getMusic).toList();
        List<Playlist> userPlaylists = user.getPlaylists();

        StringBuilder sb = new StringBuilder();

        sb.append("\n[AVAILABLE SONGS]\n");
        for (Music m : allSongs) {
            sb.append(String.format("- Id: '%d' |Title: '%s' | Artist: '%s' | Genre: '%s'\n",
                    m.getId(), m.getMusicTitle(), m.getArtist().getName(), m.getGenre().getName()));
        }

        sb.append("\n[USER PREFERENCES]\n");
        sb.append("- Followed Artists: ").append(
                followedArtists.stream().map(a -> a.getArtist().getName()).collect(Collectors.joining(", "))
        ).append("\n");

        sb.append("- Liked Songs: ").append(
                userLikedSongs.stream().map(Music::getMusicTitle).collect(Collectors.joining(", "))
        ).append("\n");

        sb.append("- Recently Played: ").append(
                userRecentSongs.stream().map(Music::getMusicTitle).collect(Collectors.joining(", "))
        ).append("\n");

        sb.append("\n[USER PLAYLISTS]\n");
        if (userPlaylists.isEmpty()) {
            sb.append("- Playlists: None\n");
        } else {
            sb.append("- Playlists: ").append(
                    userPlaylists.stream().map(Playlist::getPlaylistName).collect(Collectors.joining(", "))
            ).append("\n");
        }

        return sb.toString();
    }
}