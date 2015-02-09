package org.bu.android.db;

public abstract class AbstractBuInfoHolder<P> {

	protected P mAuthorityProvider;

	protected AbstractBuInfoHolder(P v) {
		this.mAuthorityProvider = v;
	}

}
