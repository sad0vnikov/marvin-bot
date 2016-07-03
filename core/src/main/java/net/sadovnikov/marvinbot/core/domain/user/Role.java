package net.sadovnikov.marvinbot.core.domain.user;

public abstract class Role {

    public abstract int value();
    public abstract String name();

    public boolean equals(Role role) {
        return role.value() == value();
    }

    /**
     * Check if role is higher in role hierarchy then given
     * @return boolean
     */
    public boolean lt(Role role) {
        return value() < role.value();
    }

    public boolean lt(Class<? extends Role> roleClass) {
        try {
            Role roleObj = roleClass.newInstance();
            return lt(roleObj);
        } catch (InstantiationException | IllegalAccessException e) {
            return false;
        }
    }


    /**
     * Check if role is lower in role hierarchy then given
     * @return boolean
     */
    public boolean gt(Role role) {
        return value() > role.value();
    }


    public boolean gt(Class<? extends Role> roleClass) {
        try {
            Role roleObj = roleClass.newInstance();
            return gt(roleObj);
        } catch (InstantiationException | IllegalAccessException e) {
            return false;
        }
    }

    /**
     * Check if role is higher in role hierarchy then given
     * @return boolean
     */
    public boolean lte(Role role) {
        return value() <= role.value();
    }


    public boolean lte(Class<? extends Role> roleClass) {
        try {
            Role roleObj = roleClass.newInstance();
            return lte(roleObj);
        } catch (InstantiationException | IllegalAccessException e) {
            return false;
        }
    }

    /**
     * Check if role is lower in role hierarchy then given or equal
     * @return boolean
     */
    public boolean gte(Role role) {
        return value() >= role.value();
    }


    public boolean gte(Class<? extends Role> roleClass) {
        try {
            Role roleObj = roleClass.newInstance();
            return gte(roleObj);
        } catch (InstantiationException | IllegalAccessException e) {
            return false;
        }
    }
}
