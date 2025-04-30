package dao;

import model.Livro;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class LivroDAO {

    public void salvar(Livro livro) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(livro);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void excluir(Livro livro) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(livro);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Livro buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Livro.class, id);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Livro> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Livro").list();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Livro> buscarPorCampo(String campo, String valor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Livro where " + campo + " like :valor";
            return session.createQuery(hql)
                    .setParameter("valor", "%" + valor + "%")
                    .list();
        }
    }
}
