/*
 * Copyright 2013 Otávio Gonçalves de Santana (otaviojava)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.easycassandra.persistence.cassandra;

import java.util.List;

import org.easycassandra.ClassInformation;
import org.easycassandra.ClassInformations;
import org.easycassandra.ClassInformation.KeySpaceInformation;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;

/**
 * Class to persist information in cassandra database.
 * @author otaviojava
 */
public class PersistenceSimpleImpl implements Persistence {

    private Session session;
    /**
     * when don't define the persistence will use it as keyspace.
     */
    private String keySpace;

    private RunCassandraCommand command;

    PersistenceSimpleImpl(Session session, String keySpace) {
        this.session = session;
        this.keySpace = keySpace;
        command = new RunCassandraCommand(keySpace);
        setSession();
    }

    /**
     * Constructor.
     * @param cassandraFactory the factory to cassandra
     */
    public PersistenceSimpleImpl(CassandraFactory cassandraFactory) {
        this(cassandraFactory.getSession(), cassandraFactory.getKeySpace());
    }
    /**
     * run Cassandra native query.
     * @param string the query
     */
    public void execute(String string) {
        session.execute(string);
    }

    @Override
    public <T> boolean insert(T bean) {
        return command.insert(bean, session) != null;

    }

    @Override
    public <T> boolean delete(T bean) {

        return command.delete(bean, session);
    }

    @Override
    public <T> boolean update(T bean) {
        return insert(bean);
    }

    @Override
    public <T> List<T> findAll(Class<T> bean) {
        return command.findAll(bean, session);
    }

    @Override
    public <K, T> T findByKey(K key, Class<T> bean) {
        return command.findByKey(key, bean, session);
    }

    @Override
    public <K, T> boolean deleteByKey(K key, Class<T> bean) {
        return command.deleteByKey(key, bean, session);
    }

    @Override
    public boolean executeUpdate(String query) {
        session.execute(query);
        return true;
    }

    @Override
    public <T, I> List<T> findByIndex(String indexName, I index, Class<T> bean) {

        return command.findByIndex(indexName, index, bean, session);
    }

    @Override
    public <T, I> List<T> findByIndex(I index, Class<T> bean) {

        return command.findByIndex(index, bean, session);
    }

    @Override
    public <T> Long count(Class<T> bean) {
        return command.count(bean, session);
    }

    @Override
    public <T> boolean insert(Iterable<T> beans) {
        return command.insert(beans, session);
    }

    @Override
    public <T> boolean delete(Iterable<T> beans) {
        return command.delete(beans, session);
    }

    @Override
    public <T> boolean update(Iterable<T> beans) {

        return insert(beans);
    }

    @Override
    public <K, T> List<T> findByKeys(Iterable<K> keys, Class<T> bean) {
        return command.findAll(bean, session);
    }

    @Override
    public <T> void removeAll(Class<T> bean) {
        command.removeAll(bean, session);
    }


    private void setSession() {
        session.execute("use " + keySpace);
    }

    @Override
    public <T> boolean insert(T bean, ConsistencyLevel consistency) {
        command.insert(bean, session, consistency);
        return true;
    }

    @Override
    public <T> boolean insert(Iterable<T> beans, ConsistencyLevel consistency) {
        command.insert(beans, session, consistency);
        return true;
    }

    @Override
    public <T> boolean delete(T bean, ConsistencyLevel consistency) {
        command.delete(bean, session, consistency);
        return false;
    }

    @Override
    public <T> boolean delete(Iterable<T> beans, ConsistencyLevel consistency) {
        command.delete(beans, session, consistency);
        return false;
    }

    @Override
    public <T> boolean update(T bean, ConsistencyLevel consistency) {
        return insert(bean, consistency);
    }

    @Override
    public <T> boolean update(Iterable<T> beans, ConsistencyLevel consistency) {

        return insert(beans, consistency);
    }

    @Override
    public <T> List<T> findAll(Class<T> bean, ConsistencyLevel consistency) {
        return command.findAll(bean, session, consistency);
    }

    @Override
    public <K, T> List<T> findByKeys(Iterable<K> keys, Class<T> bean,
            ConsistencyLevel consistency) {
        return command.findByKeys(keys, bean, session, consistency);
    }

    @Override
    public <K, T> T findByKey(K key, Class<T> bean, ConsistencyLevel consistency) {
        return command.findByKey(key, bean, session, consistency);
    }

    @Override
    public <K, T> boolean deleteByKey(K key, Class<T> bean,	ConsistencyLevel consistency) {
        return command.deleteByKey(key, bean, session, consistency);
    }

    @Override
    public <T, I> List<T> findByIndex(String indexName, I index, Class<T> bean,
            ConsistencyLevel consistency) {
        return command
                .findByIndex(indexName, index, bean, session, consistency);
    }

    @Override
    public <T, I> List<T> findByIndex(I index, Class<T> bean, ConsistencyLevel consistency) {

        return command.findByIndex(index, bean, session, consistency);
    }

    @Override
    public <T, I> List<T> findByKeyAndIndex(Object key, I index, Class<T> bean) {
        return command.findByKeyAndIndex(key, index, bean, session);
    }

    @Override
    public <T, I> List<T> findByKeyAndIndexRange(Object key, Object indexStart,
            Object indexEnd, boolean inclusive, Class<T> bean) {
        return command.findByKeyAndIndexRange(key, indexStart, indexEnd,
                inclusive, bean, session);
    }

    @Override
    public <T> Long count(Class<T> bean, ConsistencyLevel consistency) {
        return command.count(bean, session, consistency);
    }

    @Override
    public <T> SelectBuilder<T> selectBuilder(Class<T> classBean) {
        return new SelectBuilderImpl<>(session,
                ClassInformations.INSTACE.getClass(classBean), keySpace);
    }

    @Override
    public <T> InsertBuilder<T> insertBuilder(Class<T> classBean) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(classBean);
        KeySpaceInformation key = classInformation.getKeySpace(keySpace);
        Insert insert = QueryBuilder.insertInto(key.getKeySpace(), key.getColumnFamily());
        return new InsertBuilderImpl<>(insert, session, classInformation);
    }

    @Override
    public <T> InsertBuilder<T> insertBuilder(T classBean) {
        ClassInformation classInformation = ClassInformations.INSTACE
                .getClass(classBean.getClass());
        return new InsertBuilderImpl<>(command.createInsertStatment(classBean),
                session, classInformation);
    }

    @Override
    public <T> UpdateBuilder<T> updateBuilder(Class<T> classBean) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(classBean);
        return new UpdateBuilderImpl<>(session, classInformation, keySpace, null);
    }


    @Override
    public <T> UpdateBuilder<T> updateBuilder(Class<T> classBean, Object key) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(classBean);
        Update update = command.runUpdate(key, classBean);

        return new UpdateBuilderImpl<>(session, classInformation, keySpace,
                update);
    }

    @Override
    public <T> DeleteBuilder<T> deleteBuilder(Class<T> classBean, String... columnNames) {
        ClassInformation classInformation = ClassInformations.INSTACE.getClass(classBean);
        return new DeleteBuilderImpl<>(session, classInformation, keySpace,
                null, columnNames);
    }

    @Override
    public <T, K> DeleteBuilder<T> deleteBuilder(Class<T> classBean, K key,
            String... columnNames) {

        ClassInformation classInformation = ClassInformations.INSTACE
                .getClass(classBean);

        return new DeleteBuilderImpl<>(session, classInformation, keySpace,
                command.runDelete(key, classBean), columnNames);
    }

    @Override
    public BatchBuilder batchBuilder() {
        return new BatchBuilderImpl(session);
    }


}
