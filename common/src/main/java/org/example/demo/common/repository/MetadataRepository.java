package org.example.demo.common.repository;

import org.example.demo.common.entity.TextUploadMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetadataRepository extends JpaRepository<TextUploadMetadata, UUID> {
}