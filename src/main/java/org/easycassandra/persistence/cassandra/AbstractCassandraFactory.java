package org.easycassandra.persistence.cassandra;

import java.util.List;

import org.easycassandra.ReplicaStrategy;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
/**
 * Template of CassandraFactory.
 * @author otaviojava
 */
class AbstractCassandraFactory implements CassandraFactory {

    /**
     * Constructor to Factory.
     * @param clusterInformation {@link ClusterInformation}
     */
    public AbstractCassandraFactory(ClusterInformation clusterInformation) {
        this.clusterInformation = clusterInformation;
		initConection();
	}

    private ClusterInformation clusterInformation;

    private Cluster cluter;


    protected Cluster getCluster() {
    	return cluter;
    }

    @Override
    public List<String> getHosts() {
    	return clusterInformation.getHosts();
    }

    @Override
    public String getKeySpace() {
		return clusterInformation.getKeySpace();
	}

    @Override
    public int getPort() {
    	return clusterInformation.getPort();
    }

    @Override
    public Session getSession() {
    	return cluter.connect();
    }

    protected boolean fixColumnFamily(Session session, String familyColumn,
            Class<?> class1) {
		return new FixColumnFamily().verifyColumnFamily(session, familyColumn, class1);
	}

    @Override
    public void createKeySpace(String keySpace,
            ReplicaStrategy replicaStrategy, int factor) {
        verifyKeySpace(keySpace, getSession(), replicaStrategy, factor);
    }

    protected void verifyKeySpace(String keySpace, Session session,
            ReplicaStrategy replicaStrategy, int factor) {
		new FixKeySpace().verifyKeySpace(keySpace, session, replicaStrategy, factor);
	}
	protected void verifyKeySpace(String keySpace, Session session) {
		new FixKeySpace().verifyKeySpace(keySpace, session);
	}

	/**
	 * init the default connection.
	 */
    private  void initConection() {
        cluter = clusterInformation.build();
        new FixKeySpace().verifyKeySpace(clusterInformation.getKeySpace(), getSession());
    }

}
