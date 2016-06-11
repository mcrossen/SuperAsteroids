package edu.byu.cs.superasteroids.main_menu;


import java.util.List;

import edu.byu.cs.superasteroids.base.IView;
import edu.byu.cs.superasteroids.database.Database;
import edu.byu.cs.superasteroids.model.ShipParts.Cannon;
import edu.byu.cs.superasteroids.model.ShipParts.Engine;
import edu.byu.cs.superasteroids.model.ShipParts.ExtraPart;
import edu.byu.cs.superasteroids.model.ShipParts.MainBody;
import edu.byu.cs.superasteroids.model.ShipParts.PowerCore;
import edu.byu.cs.superasteroids.model.ShipParts.Ship;

public class MainMenuController implements IMainMenuController {

    IMainMenuView view;
    Database db;

    public MainMenuController(MainActivity main_activity)
    {
        db = new Database(main_activity);//Initialize the database
        view = main_activity;
    }

    @Override
    public void onQuickPlayPressed() {
        Ship.createNew();
        Ship ship = Ship.getInstance();

        //load the parts from the database
        List<MainBody> main_bodies = db.dao.getMainBodies();
        List<Engine> engines = db.dao.getEngines();
        List<PowerCore> power_cores = db.dao.getPowerCores();
        List<Cannon> cannons = db.dao.getCannons();
        List<ExtraPart> extra_parts = db.dao.getExtraParts();

        //find random parts to make a ship out of
        int body_rand_pos = (int)(Math.random() * (main_bodies.size()));
        int core_rand_pos = (int)(Math.random() * (power_cores.size()));
        int cannon_rand_pos = (int)(Math.random() * (cannons.size()));
        int extra_rand_pos = (int)(Math.random() * (extra_parts.size()));
        int engine_rand_pos = (int)(Math.random() * (engines.size()));

        //set the random parts to the ship
        ship.setMainBody(main_bodies.get(body_rand_pos));
        ship.setPowerCore(power_cores.get(core_rand_pos));
        ship.setCannon(cannons.get(cannon_rand_pos));
        ship.setExtraPart(extra_parts.get(extra_rand_pos));
        ship.setEngine(engines.get(engine_rand_pos));

        view.startGame();
    }

    @Override
    public IView getView() {
        //this method is merely overridden because it is abstract in the parent class.
        return null;
    }

    @Override
    public void setView(IView view) {
        //this method is merely overridden because it is abstract in the parent class.
    }
}
