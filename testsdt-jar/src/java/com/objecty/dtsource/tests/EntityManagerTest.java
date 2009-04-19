package com.objecty.dtsource.tests;

import com.objecty.dtsource.service.EntityManager;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.List;

public class EntityManagerTest extends AbstractDependencyInjectionSpringContextTests {

    //private static Logger log = Logger.getLogger(EntityManagerTest.class);

    // constructor
    public EntityManagerTest() {
        super();
        // default is autowire by type which I do not like
        // so let's set what we need manually
        setAutowireMode(AUTOWIRE_BY_NAME);
    }

    // we want auto-wire variables from loaded context by name
    // getters are not really necessary, but setters - required
    // name of variables here need to be the same like names of
    // beans in context
    // please note, that these getters and setters need to be
    // public
    public EntityManager entityHibernateManager;
    public EntityManager entityJdbcManager;

    public EntityManager getEntityHibernateManager() {
        return entityHibernateManager;
    }

    public void setEntityHibernateManager(EntityManager entityHibernateManager) {
        this.entityHibernateManager = entityHibernateManager;
    }

    public EntityManager getEntityJdbcManager() {
        return entityJdbcManager;
    }

    public void setEntityJdbcManager(EntityManager entityJdbcManager) {
        this.entityJdbcManager = entityJdbcManager;
    }

    // let know Spring where to find applicationContext
    protected String[] getConfigLocations() {
        // when running java -cp we need to have in the list of jars also name of directory
        // where applicationContext.xml is
        // see how we provide 'run' classpath in the build.xml
        return new String[]{"classpath:/demodts-war/web/WEB-INF/applicationContext.xml"};
    }

    // cannot have setUp and tearDown here - they are final

    // test if EntityHibernateManager was injected properly
    public void testEntityHibernateManagerInjected() {
        assertNotNull(getEntityHibernateManager());
    }

    // test if EntityJdbcManager was injected properly
    public void testEntityJdbcManagerInjected() {
        assertNotNull(getEntityJdbcManager());
    }

    // test if we can load all necessary hibernate-powered entities
    public void testHibernateListEntities() {
        long totalUsers = this.entityHibernateManager.sizeOfListEntities("select count(u) from User as u");
        assertEquals(11, totalUsers);       // we have only 11 records
        //log.debug("Retrieived size of list with all users");
        List listUsers = this.entityHibernateManager.listEntities("select u from User as u order by u.username", (long)0, (long)5);
        assertNotNull(listUsers);
        assertEquals(5, listUsers.size());
        //log.debug("Retrieived 5 (/5) users successfully...");
        listUsers = this.entityHibernateManager.listEntities("select u from User as u order by u.username", (long)0, (long)3);
        assertNotNull(listUsers);
        assertEquals(3, listUsers.size());
        //log.debug("Retrieived 3 (/3) users successfully...");
        listUsers = this.entityHibernateManager.listEntities("select u from User as u order by u.username", (long)0, totalUsers * 100);
        assertNotNull(listUsers);
        assertEquals(totalUsers, listUsers.size());
        //log.debug("Retrieived 11 (/15) users successfully...");
    }

    // test if we can load all necessary jdbc-powered entities
    public void testJdbcListEntities() {
        long totalUsers = this.entityJdbcManager.sizeOfListEntities("select count(*) from simple_user as u");
        assertEquals(11, totalUsers);       // we have only 11 records
        //log.debug("Retrieived size of list with all users");
        List listUsers = this.entityJdbcManager.listEntities("select * from simple_user as u order by u.suUsername", (long)0, (long)5);
        assertNotNull(listUsers);
        assertEquals(5, listUsers.size());
        //log.debug("Retrieived 5 (/5) users successfully...");                                                          
        listUsers = this.entityJdbcManager.listEntities("select * from simple_user as u order by u.suUsername", (long)0, (long)3);
        assertNotNull(listUsers);
        assertEquals(3, listUsers.size());
        //log.debug("Retrieived 3 (/3) users successfully...");
        listUsers = this.entityJdbcManager.listEntities("select * from simple_user as u order by u.suUsername", (long)0, totalUsers * 100);
        assertNotNull(listUsers);
        assertEquals(totalUsers, listUsers.size());     // we have only 11 records
        //log.debug("Retrieived 11 (/15) users successfully...");
    }

}