package com.subh.recorder.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.subh.recorder.entities.UserEntity;
import com.subh.recorder.entities.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
	List<Video> findByUser(UserEntity user);
}

