

package org.ebaloo.itkeeps.core.domain.vertex;

import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jImage;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.ExceptionPermission;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;


import java.util.List;
import java.util.stream.Collectors;


/**
 *
 *
 */
@DatabaseVertrex()
public final class fImage {


    public static jImage create(Rid requesterRid, jImage j) {

        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, Rid.NULL);

        if (!sAcl.isRoleAdmin())
            throw ExceptionPermission.NOT_ADMIN;

        // TODO Security

        vImage image = new vImage(j);

        return fImage.read(requesterRid, image.getRid().getSimple());
    }


    public static jImage delete(Rid requesterRid, Rid rid) {

        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, rid);

        if (!sAcl.isRoleAdmin())
            throw ExceptionPermission.NOT_ADMIN;

        // TODO Security

        vImage image = vImage.get(null, vImage.class, rid, false);
        jImage j = image.read();

        image.delete();

        return j;
    }

    public static jImage read(Rid requesterRid, String id) {

        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, null);

        if (!sAcl.isRoleAdmin())
            throw ExceptionPermission.NOT_ADMIN;

        // TODO Security

        vImage image = vImage.get(null, vImage.class, id, false);

        return image.read();
    }

    public static jImage update(Rid requesterRid, jImage j) {

        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, null);

        if (!sAcl.isRoleAdmin())
            throw ExceptionPermission.NOT_ADMIN;

        // TODO Security

        vImage image = vImage.get(null, vImage.class, j.getId(), false);

        image.checkVersion(j);
        image.update(j);

        return fImage.read(requesterRid, j.getId().getSimple());
    }


    public static List<jBaseLight> readAll(Rid requesterRid) {

        SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesterRid, null);

        if (!sAcl.isRoleAdmin())
            throw ExceptionPermission.NOT_ADMIN;

        // TODO Security


        return vImage.getAllBase(null, vImage.class, false).stream().map(e -> e.read(false).getLight()).collect(Collectors.toList());
    }
}


