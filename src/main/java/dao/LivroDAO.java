package dao;

import model.CampoBusca;
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

    public List<Livro> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Livro", Livro.class).list();
        }
    }

    public List<Livro> buscarPorCampo(CampoBusca campo, String valor) {
        String nomeCampo = campo.getNomeCampo();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Livro where " + nomeCampo + " like :valor";
            return session.createQuery(hql, Livro.class)
                    .setParameter("valor", "%" + valor + "%")
                    .list();
        }
    }
    public Livro buscarPorIsbn(String isbn) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Livro WHERE isbn = :isbn", Livro.class)
                    .setParameter("isbn", isbn)
                    .uniqueResult();
        }
    }

}
