package server.database;

import commons.Expense;
import commons.ExpenseTag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface ExpenseTagRepository extends JpaRepository<ExpenseTag, Long> {
    @Override
    default void flush() {

    }

    @Override
    default <S extends ExpenseTag> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    default <S extends ExpenseTag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    default void deleteAllInBatch(Iterable<ExpenseTag> entities) {

    }

    @Override
    default void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    default void deleteAllInBatch() {

    }

    @Override
    default ExpenseTag getOne(Long aLong) {
        return null;
    }

    @Override
    default ExpenseTag getById(Long aLong) {
        return null;
    }

    @Override
    default ExpenseTag getReferenceById(Long aLong) {
        return null;
    }

    @Override
    default <S extends ExpenseTag> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    default <S extends ExpenseTag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    default <S extends ExpenseTag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    default List<ExpenseTag> findAll() {
        return null;
    }

    @Override
    default List<ExpenseTag> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    default <S extends ExpenseTag> S save(S entity) {
        return null;
    }

    @Override
    default Optional<ExpenseTag> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    default boolean existsById(Long aLong) {
        return false;
    }

    @Override
    default long count() {
        return 0;
    }

    @Override
    default void deleteById(Long aLong) {

    }

    @Override
    default void delete(ExpenseTag entity) {

    }

    @Override
    default void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    default void deleteAll(Iterable<? extends ExpenseTag> entities) {

    }

    @Override
    default void deleteAll() {

    }

    @Override
    default List<ExpenseTag> findAll(Sort sort) {
        return null;
    }

    @Override
    default Page<ExpenseTag> findAll(Pageable pageable) {
        return null;
    }

    @Override
    default <S extends ExpenseTag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    default <S extends ExpenseTag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    default <S extends ExpenseTag> long count(Example<S> example) {
        return 0;
    }

    @Override
    default <S extends ExpenseTag> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    default <S extends ExpenseTag, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
