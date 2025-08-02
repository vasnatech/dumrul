package com.vasnatech.dumrul;

import com.vasnatech.commons.random.Randoms;
import com.vasnatech.dumrul.hibernate.HibernateUtil;
import com.vasnatech.dumrul.entity.sec.Authority;
import com.vasnatech.dumrul.entity.sec.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        main01();
    }

    public static void main01() {

        User user = new User();
        user.setId(Randoms.hex());
        user.setName("admin");
        user.setEmail("menderes.guven@vasna.tech");
        user.setStatus(Authority.StatusType.ACTIVE);
        user.setCreatedBy(user.getId());
        user.setCreatedAt(Instant.now());
        user.setUpdatedBy(user.getId());
        user.setUpdatedAt(Instant.now());

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the book objects
            session.persist(user);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> books = session.createQuery("from User", User.class).list();
            books.forEach(b -> System.out.println("Print user name : " + b.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
