package edu.byu.cs.superasteroids.model.ShipParts;

import edu.byu.cs.superasteroids.model.Image;

/**
 * A cannon attaches to the mainbody of the ship. For my code, the mainbody is the same as the "Ship" class
 */
public class Cannon extends ShipPart {

    /**
     * the constructor for a cannon
     * @param image         how the cannon looks
     * @param mount_point   where the cannon mounts onto the mainbody
     * @param _nozzle       where the projectiles leave the cannon from
     * @param _attack_image what the projectiles look like
     * @param _attack_sound the sound the cannon makes when it fires
     * @param _damage       how powerful the weapon is
     */
    public Cannon(Image image, MountPoint mount_point, MountPoint _nozzle, Image _attack_image, String _attack_sound, int _damage)
    {
        super(image, mount_point);
        nozzle = _nozzle;
        attack_image = _attack_image;
        attack_sound = _attack_sound;
        damage = _damage;
        attack_sound_content = -1;
    }

    @Override
    public MountPoint getBodyAttachPoint(MainBody main_body)
    {
        return main_body.getCannonAttach();
    }

    public Image getAttackImage() {
        return attack_image;
    }

    public void setAttackImage(Image attack_image) {
        this.attack_image = attack_image;
    }

    public String getAttackSound() {
        return attack_sound;
    }

    public int getAttackSoundID()
    {
        return attack_sound_content;
    }

    public void setAttackSoundID(int _id)
    {
        attack_sound_content = _id;
    }

    public void setAttackSound(String attack_sound) {
        this.attack_sound = attack_sound;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public MountPoint getNozzle() {
        return nozzle;
    }

    public void setNozzle(MountPoint nozzle) {
        this.nozzle = nozzle;
    }

    /**
     * where the image location is stored for the projectiles
     */
    private Image attack_image;

    /**
     * where the sound is stored that is played when the cannon fires
     */
    private String attack_sound;

    private int attack_sound_content;

    /**
     * the power of the cannon is stored here
     */
    private int damage;

    /**
     * the location of where the projectiles leave the cannon is stored here
     */
    private MountPoint nozzle;
}
