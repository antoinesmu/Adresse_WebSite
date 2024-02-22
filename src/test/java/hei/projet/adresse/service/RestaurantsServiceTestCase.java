package hei.projet.adresse.service;

import hei.projet.adresse.dao.RestaurantsDao;
import hei.projet.adresse.entity.Restaurant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantsServiceTestCase {
    @Mock
    private RestaurantsDao restaurantsDao;
    @InjectMocks
    private RestaurantsService restaurantsService;
    @Mock
    private RestaurantsService.InfoLibraryHolder infoLibraryHolder;

    @Test
    public void shouldListRestaurants(){
        //GIVEN
        List<Restaurant> restaurantList = new ArrayList<Restaurant>();
        Restaurant restaurant = new Restaurant(0,"test","testSpecialty","testAddress","testPhone","testWebsite",0,"testInfo");
        restaurantList.add(restaurant);

        when(restaurantsDao.listRestaurants()).thenReturn(restaurantList);

        // WHEN
        List<Restaurant> resultSet = restaurantsService.listRestaurants();
        Restaurant resultTest = resultSet.get(0);

        // THEN
        assertThat(resultTest).isNotNull();
        assertThat(resultSet.size()).isEqualTo(1);
        assertThat(resultTest.getId()).isEqualTo(0);
        assertThat(resultTest.getName()).isEqualTo("test");
        assertThat(resultTest.getSpecialty()).isEqualTo("testSpecialty");
        assertThat(resultTest.getAddress()).isEqualTo("testAddress");
        assertThat(resultTest.getPhone()).isEqualTo("testPhone");
        assertThat(resultTest.getWebsite()).isEqualTo("testWebsite");
        assertThat(resultTest.getPrice()).isEqualTo(0);
        assertThat(resultTest.getMoreInfo()).isEqualTo("testInfo");
    }

    @Test
    public void shouldGetRestaurantById(){
        //GIVEN
        Integer restaurantId = 0;
        Restaurant restaurant = new Restaurant(0,"test","testSpecialty","testAddress","testPhone","testWebsite",0,"testInfo");
        when(restaurantsDao.getRestaurantById(restaurantId)).thenReturn(restaurant);

        //WHEN
        Restaurant result = restaurantsService.getRestaurantById(restaurantId);

        //THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(0);
        assertThat(result.getName()).isEqualTo("test");
        assertThat(result.getSpecialty()).isEqualTo("testSpecialty");
        assertThat(result.getAddress()).isEqualTo("testAddress");
        assertThat(result.getPhone()).isEqualTo("testPhone");
        assertThat(result.getWebsite()).isEqualTo("testWebsite");
        assertThat(result.getPrice()).isEqualTo(0);
        assertThat(result.getMoreInfo()).isEqualTo("testInfo");
    }

    @Test
    public void shouldGetSearchResult() {
        //GIVEN
        String keyword = "test";
        List<Restaurant> restaurantList = new ArrayList<Restaurant>();
        Restaurant restaurant = new Restaurant(0,"test","testSpecialty","testAddress","testPhone","testWebsite",0,"testInfo");
        restaurantList.add(restaurant);

        when(restaurantsDao.searchResult(keyword)).thenReturn(restaurantList);

        // WHEN
        List<Restaurant> resultSet = restaurantsService.searchResult(keyword);
        Restaurant resultTest = resultSet.get(0);

        // THEN
        assertThat(resultTest).isNotNull();
        assertThat(resultTest.getId()).isEqualTo(0);
        assertThat(resultTest.getName()).isEqualTo("test");
        assertThat(resultTest.getSpecialty()).isEqualTo("testSpecialty");
        assertThat(resultTest.getAddress()).isEqualTo("testAddress");
        assertThat(resultTest.getPhone()).isEqualTo("testPhone");
        assertThat(resultTest.getWebsite()).isEqualTo("testWebsite");
        assertThat(resultTest.getPrice()).isEqualTo(0);
        assertThat(resultTest.getMoreInfo()).isEqualTo("testInfo");
    }

    @Test
    public void shouldAddRestaurant(){
        //GIVEN
        Restaurant restaurant = new Restaurant(0,"test","testSpecialty","testAddress","testPhone","testWebsite",0,"testInfo");

        //WHEN
        restaurantsService.addRestaurant(restaurant);

        //THEN
        verify(restaurantsDao, times(1)).addRestaurant(restaurant);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException(){
        //WHEN
        restaurantsService.addRestaurant(null);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionName(){
        //GIVEN
        Restaurant restaurant = new Restaurant(0,null,"testSpecialty","testAddress","testPhone","testWebsite",0,"testInfo");

        //WHEN
        restaurantsService.addRestaurant(restaurant);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionSpecialty(){
        //GIVEN
        Restaurant restaurant = new Restaurant(0,"test",null,"testAddress","testPhone","testWebsite",0,"testInfo");

        //WHEN
        restaurantsService.addRestaurant(restaurant);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionAddress(){
        //GIVEN
        Restaurant restaurant = new Restaurant(0,"test","testSpecialty",null,"testPhone","testWebsite",0,"testInfo");

        //WHEN
        restaurantsService.addRestaurant(restaurant);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionPhone(){
        //GIVEN
        Restaurant restaurant = new Restaurant(0,"test","testSpecialty","testAddress",null,"testWebsite",0,"testInfo");

        //WHEN
        restaurantsService.addRestaurant(restaurant);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWebsite(){
        //GIVEN
        Restaurant restaurant = new Restaurant(0,"test","testSpecialty","testAddress","testPhone",null,0,"testInfo");

        //WHEN
        restaurantsService.addRestaurant(restaurant);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionPrice(){
        //GIVEN
        Restaurant restaurant = new Restaurant(0,"test","testSpecialty","testAddress","testPhone","testWebsite",null,"testInfo");

        //WHEN
        restaurantsService.addRestaurant(restaurant);

        //THEN
        fail("Should throw an Illegal Argument Exception");
    }

    @Test
    public void shouldEditRestaurant(){
        //GIVEN
        Restaurant restaurant = new Restaurant(0,"test","testSpecialty","testAddress","testPhone","testWebsite",0,"testInfo");
        String name = "test";
        String specialty = "spe";
        String address = "address";
        String phone = "phone";
        String website = "website";
        Integer price = 20;
        String moreInfo = "infos";

        //WHEN
        restaurantsService.editRestaurant(restaurant,name,specialty,address,price,moreInfo,phone,website);

        //THEN
        verify(restaurantsDao, times(1)).editRestaurant(restaurant,name,specialty,address,price,moreInfo,phone,website);
    }
}
