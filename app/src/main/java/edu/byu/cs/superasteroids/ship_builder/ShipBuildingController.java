package edu.byu.cs.superasteroids.ship_builder;

import android.graphics.PointF;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.byu.cs.superasteroids.base.IView;
import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.database.Database;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.model.ShipParts.Cannon;
import edu.byu.cs.superasteroids.model.ShipParts.Engine;
import edu.byu.cs.superasteroids.model.ShipParts.ExtraPart;
import edu.byu.cs.superasteroids.model.ShipParts.MainBody;
import edu.byu.cs.superasteroids.model.ShipParts.PowerCore;
import edu.byu.cs.superasteroids.model.ShipParts.Ship;
import edu.byu.cs.superasteroids.model.ShipParts.ShipPart;

public class ShipBuildingController implements IShipBuildingController {

    private static final double SHIP_SCALE = .75;

    private IShipBuildingView.PartSelectionView state;
    private IShipBuildingView view;
    private List<Integer> main_bodies_indexes;
    private List<Integer> extra_parts_indexes;
    private List<Integer> cannons_indexes;
    private List<Integer> engines_indexes;
    private List<Integer> power_cores_indexes;
    private List<MainBody> main_bodies;
    private List<Engine> engines;
    private List<PowerCore> power_cores;
    private List<Cannon> cannons;
    private List<ExtraPart> extra_parts;
    private Ship ship;
    private boolean content_is_loaded = false;

    public ShipBuildingController(ShipBuildingActivity builder)
    {
        Database db = new Database(builder);
        view = builder;
        main_bodies = db.dao.getMainBodies();
        engines = db.dao.getEngines();
        power_cores = db.dao.getPowerCores();
        cannons = db.dao.getCannons();
        extra_parts = db.dao.getExtraParts();
        Ship.createNew(); //clean out any old gunk in the singleton.
        ship = Ship.getInstance();
    }

    @Override
    public void onViewLoaded(IShipBuildingView.PartSelectionView partView) {
        state = partView;

        List<Integer> part_ids;

        switch(state)
        {
            case MAIN_BODY:
                part_ids = main_bodies_indexes;
                view.setArrow(state, IShipBuildingView.ViewDirection.LEFT, true, "Engines");
                view.setArrow(state, IShipBuildingView.ViewDirection.UP, true, "Power Cores");
                view.setArrow(state, IShipBuildingView.ViewDirection.RIGHT, true, "Cannons");
                view.setArrow(state, IShipBuildingView.ViewDirection.DOWN, true, "Extra Parts");
                break;

            case ENGINE:
                part_ids = engines_indexes;
                view.setArrow(state, IShipBuildingView.ViewDirection.LEFT, true, "Power Cores");
                view.setArrow(state, IShipBuildingView.ViewDirection.UP, true, "Cannons");
                view.setArrow(state, IShipBuildingView.ViewDirection.RIGHT, true, "Extra Parts");
                view.setArrow(state, IShipBuildingView.ViewDirection.DOWN, true, "Main Bodies");
                break;

            case POWER_CORE:
                part_ids = power_cores_indexes;
                view.setArrow(state, IShipBuildingView.ViewDirection.LEFT, true, "Cannons");
                view.setArrow(state, IShipBuildingView.ViewDirection.UP, true, "Extra Parts");
                view.setArrow(state, IShipBuildingView.ViewDirection.RIGHT, true, "Main Bodies");
                view.setArrow(state, IShipBuildingView.ViewDirection.DOWN, true, "Engines");
                break;

            case CANNON:
                part_ids = cannons_indexes;
                view.setArrow(state, IShipBuildingView.ViewDirection.LEFT, true, "Extra Parts");
                view.setArrow(state, IShipBuildingView.ViewDirection.UP, true, "Main Bodies");
                view.setArrow(state, IShipBuildingView.ViewDirection.RIGHT, true, "Engines");
                view.setArrow(state, IShipBuildingView.ViewDirection.DOWN, true, "Power Cores");
                break;

            case EXTRA_PART:
                part_ids = extra_parts_indexes;
                view.setArrow(state, IShipBuildingView.ViewDirection.LEFT, true, "Main Bodies");
                view.setArrow(state, IShipBuildingView.ViewDirection.UP, true, "Engines");
                view.setArrow(state, IShipBuildingView.ViewDirection.RIGHT, true, "Power Cores");
                view.setArrow(state, IShipBuildingView.ViewDirection.DOWN, true, "Cannons");
                break;

            default:
                assert false;
                part_ids = new LinkedList<>();
                break;
        }

        view.setPartViewImageList(state, part_ids);

        if (ship.isComplete())
        {
            view.setStartGameButton(true);
        }
        else
        {
            view.setStartGameButton(false);
        }
    }

    @Override
    public void loadContent(ContentManager content)
    {
        if (!content_is_loaded) {
            main_bodies_indexes = new LinkedList<>();
            engines_indexes = new LinkedList<>();
            power_cores_indexes = new LinkedList<>();
            cannons_indexes = new LinkedList<>();
            extra_parts_indexes = new LinkedList<>();

            loadParts(main_bodies, main_bodies_indexes, content);
            loadParts(engines, engines_indexes, content);
            loadParts(power_cores, power_cores_indexes, content);
            loadParts(cannons, cannons_indexes, content);
            loadParts(extra_parts, extra_parts_indexes, content);

            content_is_loaded = true;
        }
    }

    private void loadParts (List<? extends ShipPart> parts, List<Integer> indexes, ContentManager content)
    {
        Iterator<? extends ShipPart> index = parts.iterator();

        while (index.hasNext())
        {
            ShipPart current_part = index.next();
            int content_id = content.loadImage(current_part.getImage().getPath());
            indexes.add(content_id);
            current_part.getImage().setContentID(content_id);
        }
    }

    @Override
    public void unloadContent(ContentManager content) {
        if (content_is_loaded) {
            unloadParts(main_bodies_indexes, content);
            unloadParts(engines_indexes, content);
            unloadParts(power_cores_indexes, content);
            unloadParts(cannons_indexes, content);
            unloadParts(extra_parts_indexes, content);

            content_is_loaded = false;
        }
    }

    void unloadParts(List<Integer> part_indexes, ContentManager content)
    {
        Iterator<Integer> index = part_indexes.iterator();

        while(index.hasNext())
        {
            content.unloadImage(index.next());
        }
    }

    @Override
    public void onSlideView(IShipBuildingView.ViewDirection direction) {

        //invert direction
        IShipBuildingView.ViewDirection new_direction;

        switch (direction)
        {
            case UP:
                new_direction = IShipBuildingView.ViewDirection.DOWN;
                break;
            case DOWN:
                new_direction = IShipBuildingView.ViewDirection.UP;
                break;
            case LEFT:
                new_direction = IShipBuildingView.ViewDirection.RIGHT;
                break;
            case RIGHT:
                new_direction = IShipBuildingView.ViewDirection.LEFT;
                break;
            default:
                assert false;
                new_direction = IShipBuildingView.ViewDirection.UP;
        }

        //next state transitions
        switch(state)
        {
            case MAIN_BODY:
                switch (new_direction) {
                    case LEFT:
                        state = IShipBuildingView.PartSelectionView.ENGINE;
                        break;
                    case UP:
                        state = IShipBuildingView.PartSelectionView.POWER_CORE;
                        break;
                    case RIGHT:
                        state = IShipBuildingView.PartSelectionView.CANNON;
                        break;
                    case DOWN:
                        state = IShipBuildingView.PartSelectionView.EXTRA_PART;
                        break;
                    default:
                        assert false;
                }
                break;

            case ENGINE:
                switch (new_direction) {
                    case LEFT:
                        state = IShipBuildingView.PartSelectionView.POWER_CORE;
                        break;
                    case UP:
                        state = IShipBuildingView.PartSelectionView.CANNON;
                        break;
                    case RIGHT:
                        state = IShipBuildingView.PartSelectionView.EXTRA_PART;
                        break;
                    case DOWN:
                        state = IShipBuildingView.PartSelectionView.MAIN_BODY;
                        break;
                    default:
                        assert false;
                }
                break;

            case POWER_CORE:
                switch (new_direction) {
                    case LEFT:
                        state = IShipBuildingView.PartSelectionView.CANNON;
                        break;
                    case UP:
                        state = IShipBuildingView.PartSelectionView.EXTRA_PART;
                        break;
                    case RIGHT:
                        state = IShipBuildingView.PartSelectionView.MAIN_BODY;
                        break;
                    case DOWN:
                        state = IShipBuildingView.PartSelectionView.ENGINE;
                        break;
                    default:
                        assert false;
                }
                break;

            case CANNON:
                switch (new_direction) {
                    case LEFT:
                        state = IShipBuildingView.PartSelectionView.EXTRA_PART;
                        break;
                    case UP:
                        state = IShipBuildingView.PartSelectionView.MAIN_BODY;
                        break;
                    case RIGHT:
                        state = IShipBuildingView.PartSelectionView.ENGINE;
                        break;
                    case DOWN:
                        state = IShipBuildingView.PartSelectionView.POWER_CORE;
                        break;
                    default:
                        assert false;
                }
                break;

            case EXTRA_PART:
                switch (new_direction) {
                    case LEFT:
                        state = IShipBuildingView.PartSelectionView.MAIN_BODY;
                        break;
                    case UP:
                        state = IShipBuildingView.PartSelectionView.ENGINE;
                        break;
                    case RIGHT:
                        state = IShipBuildingView.PartSelectionView.POWER_CORE;
                        break;
                    case DOWN:
                        state = IShipBuildingView.PartSelectionView.CANNON;
                        break;
                    default:
                        assert false;
                }
                break;
            default:
                assert false;
        }

        view.animateToView(state, new_direction);

}

    @Override
    public void onPartSelected(int index) {
        switch (state)
        {
            case MAIN_BODY:
                ship.setMainBody(main_bodies.get(index));
                break;
            case EXTRA_PART:
                ship.setExtraPart(extra_parts.get(index));
                break;
            case CANNON:
                ship.setCannon(cannons.get(index));
                break;
            case ENGINE:
                ship.setEngine(engines.get(index));
                break;
            case POWER_CORE:
                ship.setPowerCore(power_cores.get(index));
                break;
            default:
                assert false;
                break;
        }

        if (ship.isComplete())
        {
            view.setStartGameButton(true);
        }
        else
        {
            view.setStartGameButton(false);
        }
    }

    @Override
    public void onStartGamePressed() {
        view.startGame();
    }

    @Override
    public void onResume() {
        //probably don't need anything
    }

    @Override
    public IView getView() {
        return null; //perhaps leave like it is now
    }

    @Override
    public void setView(IView view) {
        //perhaps leave like it is now
    }

    @Override
    public void update(double elapsedTime) {
    }

    @Override
    public void draw() {
        final PointF gameViewCenter = new PointF(DrawingHelper.getGameViewWidth() /2, DrawingHelper.getGameViewHeight() / 2);

        ship.draw(gameViewCenter, (float)SHIP_SCALE);
    }
}
