package com.ogocx.userservice.repositories;

import com.ogocx.userservice.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {
    boolean existsByEmail(String email);
    boolean existsByFirstDocument(String firstDocument);
    boolean existsBySecondDocument(String secondDocument);

    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByFirstDocumentAndIdNot(String firstDocument, UUID id);
    boolean existsBySecondDocumentAndIdNot(String secondDocument, UUID id);
}
