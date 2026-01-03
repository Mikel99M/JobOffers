package com.joboffers.domain.offer;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class OfferRepositoryStub implements OfferRepository {

    private final Map<Long, Offer> db = new HashMap<>();
    private long idSeq = 1L;

    @Override
    public Offer save(Offer offer) {
        if (offer.getId() == null) {
            offer.setId(idSeq++);
        }
        db.put(offer.getId(), offer);
        return offer;
    }

    @Override
    public Optional<Offer> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Offer> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(Long id) {
        db.remove(id);
    }

    @Override
    public <S extends Offer> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Offer> S insert(final S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends Offer> List<S> findAll(final Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Offer> List<S> findAll(final Example<S> example, final Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Offer> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends Offer, R> R findBy(final Example<S> example, final Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> saveAll(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
    }

    @Override
    public List<Offer> findAllById(final Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(final Offer entity) {

    }

    @Override
    public void deleteAllById(final Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(final Iterable<? extends Offer> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Offer> findAll(final Sort sort) {
        return List.of();
    }

    @Override
    public Page<Offer> findAll(final Pageable pageable) {
        return null;
    }
}
