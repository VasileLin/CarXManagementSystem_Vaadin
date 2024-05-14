package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.RoleDto;
import com.vmoon.carx.dto.UserDto;
import com.vmoon.carx.entities.UserEntity;
import com.vmoon.carx.mappers.RoleMapper;
import com.vmoon.carx.mappers.UserMapper;
import com.vmoon.carx.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    Pageable pageable;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void givenUserDtoShouldSaveAndReturnSavedUser() {

        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setPassword("password");
        userDto.setRoles(Set.of(new RoleDto(1, "ADMIN")));

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword("encodedPassword");
        userEntity.setRoles(userDto.getRoles().stream().map(RoleMapper::mapToRole).collect(Collectors.toSet()));

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity userResult = userService.add(userDto);


        Assertions.assertEquals("username",userResult.getUsername());
        Assertions.assertEquals("encodedPassword",userResult.getPassword());
        assertNotNull(userResult.getRoles());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());

    }

    @Test
    void givenUserDtoShouldUpdateAndReturnUpdatedUser() {

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("updatedUsername");
        userDto.setPassword("updatedPassword");
        userDto.setRoles(Set.of(new RoleDto(1, "ADMIN")));

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword("encodedPassword");
        userEntity.setRoles(userDto.getRoles().stream().map(RoleMapper::mapToRole).collect(Collectors.toSet()));

        userEntity = UserMapper.mapToUser(userDto);

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity userResult = userService.update(userDto);

        Assertions.assertEquals("updatedUsername",userResult.getUsername());
        Assertions.assertEquals("updatedPassword",userResult.getPassword());
        assertNotNull(userResult.getRoles());
        verify(userRepository, times(1)).save(userEntity);

    }

    @Test
    void list_ShouldReturnPageWithUsers() {
        //Arrange
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("jackson");
        user.setPassword("password");
        user.setRoles(new HashSet<>());

        UserEntity user1 = new UserEntity();
        user1.setId(1);
        user1.setUsername("jackson1");
        user1.setPassword("password");
        user1.setRoles(new HashSet<>());



        List<UserEntity> users = Arrays.asList(user, user1);
        Page<UserEntity> userEntityPage = new PageImpl<>(users, pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(userEntityPage);

        //Act
        Page<UserDto> result = userService.list(pageable);

        //Assert
        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).findAll(pageable);
        assertEquals("jackson", result.getContent().get(0).getUsername());
        assertEquals("jackson1", result.getContent().get(1).getUsername());
    }

    @Test
    void listDeleted_ShouldReturnPageWithDeletedUsers() {
        //Arrange
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("jackson");
        user.setPassword("password");
        user.setDeleted(true);
        user.setRoles(new HashSet<>());

        UserEntity user1 = new UserEntity();
        user1.setId(1);
        user1.setUsername("jackson1");
        user1.setPassword("password");
        user1.setDeleted(true);
        user1.setRoles(new HashSet<>());


        List<UserEntity> deletedUsers = Arrays.asList(user, user1);
        Page<UserEntity> deletedUsersPage = new PageImpl<>(deletedUsers, pageable, deletedUsers.size());
        when(userRepository.findAllDeleted(pageable)).thenReturn(deletedUsersPage);

        //Act
        Page<UserDto> result = userService.listDeleted(pageable);

        //Assert
        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).findAllDeleted(pageable);
        assertEquals("jackson", result.getContent().get(0).getUsername());
        assertEquals("jackson1", result.getContent().get(1).getUsername());
        assertTrue(result.getContent().get(0).isDeleted());
        assertTrue(result.getContent().get(1).isDeleted());
    }

    @Test
    void findByUsername_ShouldReturnUser() {
        //Arrange
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("jackson");
        user.setPassword("password");
        user.setDeleted(true);
        user.setRoles(new HashSet<>());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        //Act
        UserEntity findUser = userService.findByUsername(user.getUsername());

        //Assert
        assertNotNull(findUser);
        assertEquals("jackson", findUser.getUsername());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }
}