package com.ifsc.secstor.api.repository;

import com.ifsc.secstor.api.model.Role;
import com.ifsc.secstor.api.model.UserModel;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageImpl;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static UserModel admin;
    private static UserModel client1;
    private static UserModel client2;

    @BeforeAll
    static void init() {
        admin = new UserModel(1L, "admin", "admin", Role.ADMINISTRATOR);
        client1 = new UserModel(2L, "client1", "client1", Role.CLIENT);
        client2 = new UserModel(3L, "client2", "client2", Role.CLIENT);
    }

    @AfterAll
    static void close() {
        admin = null;
        client1 = null;
        client2 = null;
    }

    @BeforeEach
    void setUp() {
        this.userRepository.save(admin);
        this.userRepository.save(client1);
    }

    @AfterEach
    void tearDown() {
        this.userRepository.deleteAll();
    }

    //Assert Equals
    @Test
    void findByUsername_RegisteredUsername_UserModel() {
        //given
        String username = "admin";

        //when
        var result = this.userRepository.findByUsername(username);

        //then
        assertThat(result.getUsername()).isEqualTo(admin.getUsername());
    }

    //Assert Not Equals
    @Test
    void findByUsername_RegisteredButDifferentUsername_UserModel() {
        //given
        String username = "client1";

        //when
        var result = this.userRepository.findByUsername(username);

        //then
        assertThat(result.getUsername()).isNotEqualTo(admin.getUsername());
    }

    //Assert Null
    @Test
    void findByUsername_NotRegisteredUsername_Null() {
        //given
        String username = "acacio";

        //when
        var result = this.userRepository.findByUsername(username);

        //then
        assertThat(result).isNull();
    }

    //Assert Throws
    @Test
    void findByRoleAndUsername_DifferentRole_NullPointerException() {
        //given
        String username = "client2";

        //when
        var result = this.userRepository.findByRoleAndUsername(Role.ADMINISTRATOR, username);

        //then
        assertThatThrownBy(() -> assertThat(result.getUsername()).isEqualTo(client2.getUsername()))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    @Disabled
    void findByRoleAndUsername_ValidRoleAndUsername_UserModel() {
        //given
        String username = "admin";
        Role role = Role.ADMINISTRATOR;

        //when
        var result = this.userRepository.findByRoleAndUsername(role, username);

        //then
        assertThat(result).isExactlyInstanceOf(UserModel.class);
    }

    @Test
    @Timeout(value = 300, unit = TimeUnit.MILLISECONDS)
    void findAllByRole_ClientRole_PageOfUserModel() {
        //given
        Role role = Role.CLIENT;

        //then
        this.userRepository.findAllByRole(role, null);
    }

    //Assertion Grouping
    @Test
    void findAllByRole_AdminRole_PageOfUserModel() {
        //given
        Role role = Role.ADMINISTRATOR;

        //when
        var result = this.userRepository.findAllByRole(role, null);

        //then
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(result).isExactlyInstanceOf(PageImpl.class);
        assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        assertions.assertThat(result.getContent().get(0).getUsername()).isEqualTo(admin.getUsername());
        assertions.assertThat(result.getContent().get(0).getPassword()).isEqualTo(admin.getPassword());
        assertions.assertThat(result.getContent().get(0).getRole()).isEqualTo(admin.getRole());

        assertions.assertAll();
    }

    //AssumeTrue
    @Test
    void existsByUsername_RegisteredUsername_True() {
        //given
        String username = "admin";

        //when
        boolean expected = this.userRepository.existsByUsername(username);

        //then
        assertThat(expected).isTrue();
    }

    //AssumeFalse
    @Test
    void existsByUsername_NotRegisteredUsername_False() {
        //given
        String username = "acacio";

        //when
        boolean expected = this.userRepository.existsByUsername(username);

        //then
        assertThat(expected).isFalse();
    }
}