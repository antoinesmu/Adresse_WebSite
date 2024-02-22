package hei.projet.adresse.service;

import hei.projet.adresse.dao.UsersDao;
import hei.projet.adresse.entity.Restaurant;
import hei.projet.adresse.entity.ReviewWithLogin;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.LoginAlreadyExistingException;
import hei.projet.adresse.exception.users.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UsersServiceTestCase {
    @Mock
    private UsersDao userDao;
    @InjectMocks
    private UsersService usersService;
    @Mock
    private RestaurantsService.InfoLibraryHolder infoLibraryHolder;
    @Mock
    private ReviewService reviewService;


    @Test
    public void shouldGetUserById() throws UserNotFoundException {
        //Give
        Users user = new Users(0, "testCase","pwd", "firstName", "lastName", "test.case@adresse.fr", false);
        when(userDao.getUserById(0)).thenReturn(user);

        //When
        Users result = usersService.getUserById(0);

        //Then
        Assertions.assertThat(result).isEqualTo(user);
    }
    @Test (expected = UserNotFoundException.class)
    public void shouldGetUserByIdAndThrowUserNotFoundException() throws UserNotFoundException {
        //Give
        when(usersService.getUserById(0)).thenThrow(UserNotFoundException.class);

        //When
        usersService.getUserById(0);

        //Then
        fail("Should throw an UserNotFoundException");
    }
    @Test
    public void shouldGetUserByLogin() throws UserNotFoundException {
        //Give
        Users user = new Users(1, "existingLogin","pwd", "firstName", "lastName", "email@adresse.fr", false);
        when(userDao.getUserByLogin("existingLogin")).thenReturn(user);

        //When
        Users result = usersService.getUserByLogin("existingLogin");

        //Then
        Assertions.assertThat(result).isEqualTo(user);
    }
    @Test (expected = UserNotFoundException.class)
    public void shouldGetUserByLoginAndThrowUserNotFoundException() throws UserNotFoundException {
        //Give
        when(usersService.getUserByLogin("login")).thenThrow(UserNotFoundException.class);

        //When
        usersService.getUserByLogin("login");

        //Then
        fail("Should throw an UserNotFoundException");
    }

    @Test
    public void shouldAddNewUser() throws LoginAlreadyExistingException {
        //Give
        List<Users> usersList = new ArrayList<>();
        Users user = new Users(0, "testCase","pwd", "firstName", "lastName", "test.case@adresse.fr", false);
        when(userDao.addUser(user)).thenReturn(user);

        //When
        usersList.add(usersService.addUser(user));

        //Then
        Assertions.assertThat(usersList).containsExactlyInAnyOrder(user);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldAddAndThrowIllegalArgumentLogin() throws LoginAlreadyExistingException {
        //GIVEN
        Users user = new Users(0, null,"pwd", "firstName", "lastName", "test.case@adresse.fr", false);

        //WHEN
        usersService.addUser(user);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldAddAndThrowIllegalArgumentPassword() throws LoginAlreadyExistingException {
        //GIVEN
        Users user = new Users(0, "testCase",null, "firstName", "lastName", "test.case@adresse.fr", false);

        //WHEN
        usersService.addUser(user);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldAddAndThrowIllegalArgumentFirstName() throws LoginAlreadyExistingException {
        //GIVEN
        Users user = new Users(0, "testCase","pwd", null, "lastName", "test.case@adresse.fr", false);

        //WHEN
        usersService.addUser(user);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldAddAndThrowIllegalArgumentLastName() throws LoginAlreadyExistingException {
        //GIVEN
        Users user = new Users(0, "testCase","pwd", "firstName", null, "test.case@adresse.fr", false);

        //WHEN
        usersService.addUser(user);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldAddAndThrowIllegalArgumentEmail() throws LoginAlreadyExistingException {
        //GIVEN
        Users user = new Users(0, "testCase","pwd", "firstName", "lastName", null, false);

        //WHEN
        usersService.addUser(user);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = LoginAlreadyExistingException.class)
    public void shouldAddAndThrowLoginAlreadyExistingException() throws LoginAlreadyExistingException {
        //GIVEN
        Users user = new Users(0, "testCase","pwd", "firstName", "lastName", "email", false);
        when(userDao.checkSimilarLogin(user)).thenReturn(true);

        //WHEN
        usersService.addUser(user);

        //THEN
        fail("Should throw an Login Already Existing Exception");
    }

    @Test
    public void shouldDeleteExistingUser() throws UserNotFoundException {
        //GIVEN
        Users user = new Users(0, "testCase","pwd", "firstName", "lastName", "email", false);
        List<ReviewWithLogin> listReview = new ArrayList<>();
        ReviewWithLogin review1 = new ReviewWithLogin(1,1,1,"loginTest1","titreTest1","champTest1",5);
        listReview.add(review1);
        when(reviewService.listReviewByUserId(user.getId())).thenReturn(listReview);

        //WHEN
        usersService.deleteUser(user);

        //THEN
        verify(userDao, times(1)).deleteUser(user);
        verify(reviewService, times(1)).deleteReview(review1);
    }


    @Test (expected = UserNotFoundException.class)
    public void shouldDeleteUserAndThrowUserNotFoundException() throws UserNotFoundException {
        //GIVEN
        Users user = new Users(0, "testCase","pwd", "firstName", "lastName", "email", false);
        when(reviewService.listReviewByUserId(user.getId())).thenThrow(UserNotFoundException.class);

        //WHEN
        usersService.deleteUser(user);

        //THEN
        fail("Should throw an UserNotFoundException");

    }

    @Test
    public void shouldChangePrivilege() throws UserNotFoundException {
        //GIVEN
        Users user = new Users(0, "testCase","pwd", "firstName", "lastName", "email", false);
        when(usersService.getUserById(0)).thenReturn(user);

        //WHEN
        usersService.changePrivilege(0);

        //THEN
        verify(userDao, times(1)).changePrivilege(0);
    }

    @Test (expected = UserNotFoundException.class)
    public void shouldChangePrivilegeAndThrowUserNotFoundException() throws UserNotFoundException {
        //GIVEN
        Users user = new Users(0, "testCase","pwd", "firstName", "lastName", "email", false);
        when(usersService.getUserById(user.getId())).thenThrow(UserNotFoundException.class);

        //WHEN
        usersService.changePrivilege(user.getId());

        //THEN
        fail("Should throw an UserNotFoundException");
    }

    @Test
    public void shouldListAllUsers() {
        //Give
        List<Users> usersList = new ArrayList<>();
        Users user = new Users(0, "testCase","pwd", "firstName", "lastName", "test.case@adresse.fr", false);
        usersList.add(user);
        when(userDao.listUsers()).thenReturn(usersList);

        //When
        List<Users> result = usersService.listUsers();

        //Then
        Assertions.assertThat(result).isEqualTo(usersList);
    }

    @Test
    public void shouldVerifyPasswordAndReturnTrue() {
        //Give
        Users user = new Users(0, "testCase","pwdTest", "firstName", "lastName", "test.case@adresse.fr", false);
        String testPwdHashed = "$argon2i$v=19$m=65536,t=22,p=1$xgzWmcZaYnGmzMWptuv5+w$Gx7+F+RfsJf5iRgmFa/ZeTRK5/2C6GeQF6t+sYL77o8";

        //When
        Boolean resultByUser = usersService.verifyPassword(user, "pwdTest");
        Boolean resultByString = usersService.verifyPassword(testPwdHashed, "testPwd");

        //Then
        Assertions.assertThat(resultByUser).isTrue();
        Assertions.assertThat(resultByString).isTrue();
    }
    @Test
    public void shouldVerifyPasswordAndReturnFalse() {
        //Give
        Users user = new Users(0, "testCase","pwdTest", "firstName", "lastName", "test.case@adresse.fr", false);
        String testPwdHashed = "$argon2i$v=19$m=65536,t=22,p=1$xgzWmcZaYnGmzMWptuv5+w$Gx7+F+RfsJf5iRgmFa/ZeTRK5/2C6GeQF6t+sYL77o8";

        //When
        Boolean resultByUser = usersService.verifyPassword(user, "wrongPassword");
        Boolean resultByString = usersService.verifyPassword(testPwdHashed, "wrongPassword");

        //Then
        Assertions.assertThat(resultByUser).isFalse();
        Assertions.assertThat(resultByString).isFalse();
    }

    @Test
    public void shouldEncodePassword() {
        //Give
        String testPwd = "testPwd";

        //When
        Boolean result = usersService.verifyPassword(usersService.encode(testPwd), testPwd);

        //Then
        Assertions.assertThat(result).isTrue();
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldEncodeAndThrowIllegalArgument(){
        //GIVEN
        Users user = new Users(0, "testCase",null, "firstName", "lastName", "test.case@adresse.fr", false);

        //WHEN
        usersService.encode(user.getPassword());

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test
    public void shouldChangePasswordAndSucceed() {
        //Give
        Users user = new Users(0, "testCase","oldPwd", "firstName", "lastName", "test.case@adresse.fr", false);
        String newPassword = "newPwd";

        //When
        Boolean result = usersService.changePassword(user, "oldPwd", newPassword);
        Boolean match = usersService.verifyPassword(user, newPassword);

        //Then
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(match).isTrue();

    }
    @Test
    public void shouldChangePasswordAndFail() {
        //Give
        Users user = new Users(0, "testCase","oldPwd", "firstName", "lastName", "test.case@adresse.fr", false);

        //When
        Boolean result = usersService.changePassword(user, "wrongOldPassword", "newPassword");
        Boolean match = usersService.verifyPassword(user, "oldPwd");

        //Then
        Assertions.assertThat(result).isFalse();
        Assertions.assertThat(match).isTrue();
    }
}
