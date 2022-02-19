package playground;

import neural.NetworkEventsListener;
import playground.world.Sheep;
import playground.world.Wolf;
import playground.world.World;
import playground.world.swingdrawer.WorldDrawer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldPlayground {

    public static void main(String[] args) throws IOException, URISyntaxException {

        int cellSize = 10;

        JFrame worldFrame = new JFrame();
        worldFrame.setBackground(Color.BLACK);
        worldFrame.setPreferredSize(new Dimension(1600, 700));

        WorldDrawer worldDrawer = new WorldDrawer(cellSize);
        worldFrame.add(worldDrawer);
        worldFrame.pack();
        worldFrame.setLocationRelativeTo(null);
        worldFrame.setVisible(true);

        Random random = new Random();
        int halfSize = 4;

        int xSize = 100;
        int ySize = 60;

//        List<NetworkEventsListener> listeners = List.of(
//                new LayoutAdapter(new GraphStreamStaticLayout()),
//                new LayoutAdapter(new GraphStreamDynamicLayout()));

//        List<NetworkEventsListener> listeners = List.of(new LayoutAdapter(new GraphStreamStaticLayout()));
        List<NetworkEventsListener> listeners = new ArrayList<>();

        Sheep sheep = Sheep.newBuilder(random.nextInt(), -halfSize, -halfSize, halfSize, halfSize)
                .withNetworkLimitSize(20000)
                .build(listeners);

        World.Builder builder = World.newBuilder(xSize, ySize)
//                .withSleep(300)
                .addObject(sheep, xSize / 2, ySize / 2);
        for (int i = 0; i < xSize * ySize * 0.02; i++) {
            builder.addObject(new Wolf(random.nextInt()), Math.abs(random.nextInt()) % xSize, Math.abs(random.nextInt()) % ySize);
        }
        builder.build(worldDrawer);

    }

}
