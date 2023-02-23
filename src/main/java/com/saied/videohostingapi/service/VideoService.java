package com.saied.videohostingapi.service;

import com.saied.videohostingapi.models.Video;
import com.saied.videohostingapi.models.Comment;
import com.saied.videohostingapi.models.marks.Dislike;
import com.saied.videohostingapi.models.marks.Like;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    /**
     * Uploading video file
     * @param channelId Channel id
     * @param file Video file
     * @return Video entity
     */
    Video uploadVideo(Long channelId, MultipartFile file);

    /**
     * Updating video's name
     * @param videoId Video id
     * @param name New video name
     */
    void updateName(Long videoId, String name);

    /**
     * Finding video
     * @param id Video id
     * @return Video entity
     */
    Video findById(Long id);

    /**
     * Finding like
     * @param id Like id
     * @return Like entity
     */
    Like findLikeById(Long id);

    /**
     * Finding dislike
     * @param id Dislike id
     * @return Dislike entity
     */
    Dislike findDislikeById(Long id);

    /**
     * Finding comment
     * @param id Comment id
     * @return comment entity
     */
    Comment findCommentById(Long id);

    /**
     * Listing videos with pagination
     * @param page Number of the page
     * @return List of videos
     */
    List<Video> getVideosPaginated(int page);

    /**
     * Listing likes of provided video with pagination
     * @param videoId Video id
     * @param page page number
     * @return List of dikes
     */
    List<Like> getLikesPaginated(Long videoId, int page);

    /**
     * Listing dislikes of provided video with pagination
     * @param videoId Video id
     * @param page page number
     * @return List of dislikes
     */
    List<Dislike> getDislikesPaginated(Long videoId, int page);

    /**
     * Listing comments of provided video with pagination
     * @param videoId Video id
     * @param page page number
     * @return List of comments
     */
    List<Comment> getCommentsPaginated(Long videoId, int page);

    /**
     * Setting like for provided user
     * @param profileId Profile id
     * @param videoId Video id
     */
    void setLike(Long profileId, Long videoId);

    /**
     * Setting dislike for provided user
     * @param profileId Profile id
     * @param videoId Video id
     */
    void setDislike(Long profileId, Long videoId);

    /**
     * Setting comment for provided user
     * @param profileId Profile id
     * @param videoId Video id
     * @param commentBody Body of comment
     */
    void saveComment(Long profileId, Long videoId, String commentBody);

    /**
     * Deleting video
     * @param id VideoId
     */
    void delete(Long id);

    /**
     * Deleting like that was set by provided user
     * @param profileId Profile id
     * @param videoId Video id
     */
    void deleteLike(Long profileId, Long videoId);

    /**
     * Deleting dislike that was set by provided user
     * @param profileId Profile id
     * @param videoId Video id
     */
    void deleteDislike(Long profileId, Long videoId);

    /**
     * Deleting comment that was set by provided user
     * @param profileId Profile id
     * @param videoId Video id
     */
    void deleteComment(Long profileId, Long videoId);

}
