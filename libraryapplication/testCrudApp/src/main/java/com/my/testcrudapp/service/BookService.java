package com.my.testcrudapp.service;

import com.my.testcrudapp.hibernate.HibernateUtils;
import com.my.testcrudapp.model.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.swagger.v3.oas.annotations.*;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final RestTemplate restTemplate;

    @Autowired
    public BookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    public Book findById(Long id){
        Book book = HibernateUtils.startSession().get(Book.class, id);
        HibernateUtils.closeSession();
        return book;
    }

    public void saveBook(Book book){
        Session session = HibernateUtils.startSession();
        session.getTransaction().begin();
        session.persist(book);
        session.getTransaction().commit();
        HibernateUtils.closeSession();
    }

   public List<Book> findAll(){
        List<Book> books = HibernateUtils.startSession().createQuery("FROM Book").list();
        HibernateUtils.closeSession();
        return books;
    }

    public void deleteById(Long id){
        Book book = HibernateUtils.startSession().get(Book.class, id);
        HibernateUtils.getSession().beginTransaction();
        HibernateUtils.getSession().remove(book);
        HibernateUtils.getSession().getTransaction().commit();
        HibernateUtils.closeSession();
    }

    @Operation(summary = "Поиск книги по ISBN")
    public List<Book> findByIsbn(String isbn){
        String hql = "FROM Book WHERE isbn = :name";
        Query<Book> query = HibernateUtils.startSession().createQuery(hql);
        query.setParameter("name", isbn);
        List<Book> books = query.getResultList();
        HibernateUtils.closeSession();
        return books;
    }

    public List<Book> getFreeBookList(){
        List<Book> books = findAll();
        List<Book> freeBooks = new ArrayList<>();
        for(Book book : books){
            if(restTemplate.postForObject("http://localhost:8081/library/status", book.getId(), boolean.class)){
                freeBooks.add(book);
            }
        }
        return freeBooks;
    }

    public void updateBook(Book book){
        Session session = HibernateUtils.startSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            HibernateUtils.closeSession();
        }
    }

}
