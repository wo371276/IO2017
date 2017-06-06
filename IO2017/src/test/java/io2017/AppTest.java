package io2017;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import io2017.categories.CategoriesRepository;
import io2017.categories.Category;
import io2017.categories.CategoryService;
import io2017.dictionaries.Dictionary;
import io2017.dictionaries.DictionaryRepository;
import io2017.helpers.Language;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CategoriesRepository categoriesRepository;
    
    @Autowired
    private DictionaryRepository dictionaryRepository;
    
    @Autowired
    private CategoryService categoryService;
    
//    @InjectMocks
//    UserListController controller;
//    
//    @Before
//    public void setUp() throws Exception {
//
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/jsp/view/");
//        viewResolver.setSuffix(".jsp");
//
//        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
//                .setViewResolvers(viewResolver)
//                    .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
//    }
    
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/"))
        		.andExpect(status().isOk())
                .andExpect(content().string(containsString("Tutaj można się zalogować")));
    }
    
    @Test
    public void shouldOpenLoginView() throws Exception {
        this.mockMvc.perform(get("/login"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("login"))
        		.andExpect(content().string(containsString("Login")))
        		.andExpect(content().string(containsString("Hasło")));
    }
    
    @Test
    public void shouldOpenRegistrationView() throws Exception {
        this.mockMvc.perform(get("/register/new"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("register_new"))
        		.andExpect(content().string(containsString("Rejestracja")))
        		.andExpect(content().string(containsString("Potwierdź hasło")));
    }
    
    @Test
    @WithMockUser
    public void shouldOpenCategoriesView() throws Exception {
        this.mockMvc.perform(get("/categories"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("categories"))
        		.andExpect(content().string(containsString("Kategorie")));
    }
    
    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldOpenCategoriesViewForAdmin() throws Exception {
        this.mockMvc.perform(get("/admin/categories"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("admin_categories"));
    }
    
    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldOpenNewCategoryViewForAdmin() throws Exception {
        this.mockMvc.perform(get("/admin/categories/newCategory"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("create_category"));
    }
    
//    @Test
//    @WithMockUser
//    public void shouldOpenQuizOptionsView() throws Exception {
//        this.mockMvc.perform(get("/quiz/options?id=3")).andDo(print())
//        		.andExpect(status().isOk())
//        		.andExpect(view().name("quiz_options"));
//    }
    
    @Test
    @WithMockUser
    public void shouldOpenQuizOpenView() throws Exception {
        this.mockMvc.perform(get("/quiz/open?id=3&mode=FROM_POLISH&number=9"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("quiz_open"));
    }
    
    @Test
    @WithMockUser
    public void shouldOpenQuizABCDView() throws Exception {
        this.mockMvc.perform(get("/quiz/closed?id=3&mode=FROM_POLISH&number=9"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("quiz_abcd"));
    }
    
    
    @Test
    @WithMockUser
    public void shouldOpenDictionariesView() throws Exception {
        this.mockMvc.perform(get("/dictionaries"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("admin_dictionaries"));
    }
    
    @Test
    @WithMockUser
    public void shouldOpenNewDictionariesView() throws Exception {
        this.mockMvc.perform(get("/dictionaries/newDictionary"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("create_dictionary"));
    }
    
    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldOpenDictionariesViewForAdmin() throws Exception {
        this.mockMvc.perform(get("/admin/dictionaries"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("admin_dictionaries"));
    }
    
    @Test
    @WithMockUser
    public void shouldOpenSearchView() throws Exception {
        this.mockMvc.perform(get("/searchUsers?name=a"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("searchUsers"));
    }
    
//    @Test
//    @WithMockUser
//    public void shouldOpenProfileView() throws Exception {
//        this.mockMvc.perform(get("/profil?login=asd")).andDo(print())
//        		.andExpect(status().isOk())
//        		.andExpect(view().name("profile"));
//    }
    
//    @Test
//    @WithMockUser(roles="ADMIN")
//    public void shouldOpenUsersViewForAdmin() throws Exception {
//        this.mockMvc.perform(get("/admin/users")).andDo(print())
//        		.andExpect(status().isOk())
//        		.andExpect(view().name("admin_users"));
//    }
    
    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldOpenNewUserViewForAdmin() throws Exception {
        this.mockMvc.perform(get("/admin/users/newUser"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("create_user"));
    }
    
    @Test
    @WithMockUser(roles="ADMIN")
    public void shouldOpenEditUserViewForAdmin() throws Exception {
        this.mockMvc.perform(get("/admin/users/editUser?id=2"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("edit_user"));
    }
    
    @Test
    public void testDeleteCategory() throws Exception {
    	String firstCategoryName = "kategoria nr 1";
    	String secondCategoryName = "kategoria nr 2";
    	String dictionaryName = "nazwa slownika";
    	
    	if(categoriesRepository.findByName(firstCategoryName) == null) {
	    	Category category1 = new Category();
	    	category1.setName(firstCategoryName);
    		categoriesRepository.save(category1);
    	}
    	
    	if(categoriesRepository.findByName(secondCategoryName) == null) {
        	Category category2 = new Category();
        	category2.setName(secondCategoryName);
        	
    		categoriesRepository.save(category2);
    	}
    	
    	Category cat = categoriesRepository.findByName(secondCategoryName);
    	
    	if(dictionaryRepository.findByName(dictionaryName) == null) {
    		Dictionary newDictionary = new Dictionary();
        	newDictionary.setCategory(cat);
        	newDictionary.setLanguage(Language.ENGLISH.getName());
        	newDictionary.setName(dictionaryName);
        	newDictionary.setUser(null);
    		dictionaryRepository.save(newDictionary);
    	} else {
    		Category categoryToSet = categoriesRepository.findByName(secondCategoryName);
    		Dictionary dic = dictionaryRepository.findByName(dictionaryName);
    		dic.setCategory(categoryToSet);
    		dictionaryRepository.save(dic);
    	}
    	
    	Dictionary dictionary = dictionaryRepository.findByName(dictionaryName);
    	String dictionaryCategoryName = dictionary.getCategory().getName();
    	
    	//teraz słownik jest w kategorii nr 2
    	Assert.assertEquals(secondCategoryName, dictionaryCategoryName);
    	
    	categoryService.changeCategoriesForDelete(categoriesRepository.findByName(secondCategoryName).getCategoryId());
    	categoryService.deleteCategory(categoriesRepository.findByName(secondCategoryName).getCategoryId());
    	
    	//usunęliśmy kategorię nr 2, więc słownik powinien być w kategorii o najniższym id
    	List<Category> allCategories = (List<Category>) categoriesRepository.findAll();
    	
    	Long lowestCategoryId = (long) 999999;
    	
    	for(Category c : allCategories) {
    		lowestCategoryId = Math.min(lowestCategoryId, c.getCategoryId());
    	}
    	
    	Category lowestCategory = categoriesRepository.findOne(lowestCategoryId);
    	
    	dictionary = dictionaryRepository.findByName(dictionaryName);
    	dictionaryCategoryName = dictionary.getCategory().getName();
    	
    	Assert.assertEquals(lowestCategory.getName(), dictionaryCategoryName);
    }
    
}
