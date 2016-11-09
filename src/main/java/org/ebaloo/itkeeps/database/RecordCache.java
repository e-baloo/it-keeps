package org.ebaloo.itkeeps.database;

import com.orientechnologies.orient.core.cache.ORecordCache;
import com.orientechnologies.orient.core.db.record.ridbag.ORidBag;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.WeakHashMap;

public class RecordCache implements ORecordCache {

    private static boolean enabled = true;
    private final static WeakHashMap<ORID, WeakReference<ORCache>> cache = new WeakHashMap<>();
    private static long expiration = 300000;

    private static class ORCache {
        private final ORecord record;
        private final long expiration;
        ORCache(ORecord record) {
            this.record = record;
            this.expiration = System.currentTimeMillis() + RecordCache.expiration;
        }
    }

    public void setExpiration(long time) {
        expiration = time;
    }

    @Override
    public ORecord get(final ORID rid) {
        if (!isEnabled())
            return null;
        return get(cache.get(rid));
    }

    @Override
    public ORecord put(final ORecord record) {
        if (!isEnabled())
            return null;
        return get(cache.put(record.getIdentity(), new WeakReference<>(new ORCache(record))));
    }

    @Override
    public ORecord remove(final ORID rid) {
        if (!isEnabled())
            return null;
        // Fix cache bug
        // Cas: (V1) -> (E1) -> (V2) -> (E2) -> (V3) avec suppression de l'edge (E2)
        // On doit supprimer du cache V2 et V3 car ils contiennent une reference a E2
        // On doit EGALEMENT supprimer E1 car E1 stocke une copie de V2 qui peut mettre a defaut le cache quand on veut
        // recuperer les Vertices de V1
        ORecord oRecord = get(rid); //Note: Utiliser plutot rid.getRecord() plutot que tenter de le retrouver dans le cache
        if(oRecord != null && oRecord instanceof ODocument) {
            Arrays.stream(((ODocument) oRecord).fieldValues())
                    .forEach(e -> {
                        if(e instanceof ORidBag) {
                            ((ORidBag) e).forEach(f -> {if(f != null) cache.remove(f.getIdentity()); });
                        } else if(e instanceof ORecordId) {
                            Arrays.stream(((ODocument) ((ORecordId)e).getRecord()).fieldValues())
                                    .filter(f -> f instanceof ORidBag)
                                    .forEach(f -> ((ORidBag)f).forEach(g -> {if(g != null) cache.remove(g.getIdentity()); }));
                            cache.remove(((ORecordId)e).getIdentity());
                        }
                    });
        }
        return get(cache.remove(rid));
    }

    private ORecord get(WeakReference<ORCache> value) {
        if (value == null)
            return null;
        else {
            ORCache ocache = value.get();
            if(ocache == null) {
                return null;
            } else if(System.currentTimeMillis() > ocache.expiration) {
                cache.remove(ocache.record.getIdentity());
                return null;
            } else {
                return ocache.record;
            }
        }
    }

    @Override
    public void shutdown() {
        cache.clear();
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public void startup() {
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean disable() {
        return enabled = false;
    }

    @Override
    public boolean enable() {
        return enabled = true;
    }

    @Override
    public Collection<ORID> keys() {
        return new ArrayList<>(cache.keySet());
    }

}
