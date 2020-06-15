package tools.finance;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GdxGUI extends ApplicationAdapter {

    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private int percentSum = 0;
    private Label percentSumLabel;

    private PortfolioTableRow selectedPortfolio;
    private Image focusIndicator;

    private static final int TEXT_HEIGHT = 52;
    private static final int BTN_WIDTH = 360;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
    private Config config = new Config();

    public static void main(String[] args) {

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "401k Scenarios";
        cfg.width = 1080;
        cfg.height = 1920;
        new LwjglApplication(new GdxGUI(), cfg);
    }

    @Override
    public void create() {

        this.batch = new SpriteBatch();

        this.stage = new Stage();
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        Image background = new Image(fillRectangle(300, 10, Color.GRAY, .75f));

        focusIndicator = new Image(fillRectangle(BTN_WIDTH * 1, TEXT_HEIGHT, Color.YELLOW, .35f));
        focusIndicator.setWidth(BTN_WIDTH * 1);
        focusIndicator.setHeight(TEXT_HEIGHT);

        percentSumLabel = new Label("", skin);

        Table portfolioContainer = new Table();
        portfolioContainer.debug();
        portfolioContainer.setFillParent(true);

        Table portfolioHdrTable = new Table();

        portfolioHdrTable.setBackground(background.getDrawable());
        portfolioHdrTable.pad(1).defaults().expandX().space(4);
        portfolioHdrTable.row();
        portfolioHdrTable.add(new Label("Portfolio Name", skin));
        portfolioHdrTable.add(new Label("Value", skin));

        final Table portfolioTable = new Table();

        portfolioTable.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {

                if (event.toString().equals("touchDown")) {

                    if (focusIndicator.getParent() != null) {
                        focusIndicator.getParent().removeActor(focusIndicator);
                    }

                    if (event.getTarget() instanceof PortfolioTableRow) {
                        selectedPortfolio = (PortfolioTableRow) event.getTarget();
                        selectedPortfolio.addActor(focusIndicator);
                    } else if (event.getTarget().getParent() instanceof PortfolioTableRow) {
                        selectedPortfolio = (PortfolioTableRow) event.getTarget().getParent();
                        selectedPortfolio.addActor(focusIndicator);
                    }
                }

                return false;
            }
        }
        );

        TextButton addPortButton = new TextButton("Add Portfolio", skin);
        addPortButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                portfolioTable.row().pad(2);

                PortfolioTableRow row = new PortfolioTableRow("PORTFOLIO");
                portfolioTable.add(row).height(TEXT_HEIGHT).left().expandX();

                List<StockTableRow> stocks = new ArrayList<>();
                row.setUserObject(stocks);
            }
        });

        TextButton deletePortButton = new TextButton("Delete Portfolio", skin);
        deletePortButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            }
        });

        TextButton calcButton = new TextButton("Calculate", skin);
        addPortButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            }
        });

        TextButton saveButton = new TextButton("Save", skin);
        saveButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            }
        });

        TextButton loadButton = new TextButton("Load", skin);
        loadButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            }
        });

        TextField initialDateField = new TextField("", skin);
        TextField initialBalanceField = new TextField("", skin);
        TextField monthlyContribField = new TextField("", skin);

        portfolioContainer.add(portfolioHdrTable).height(TEXT_HEIGHT).expand().fill().colspan(3);

        portfolioContainer.row();
        ScrollPane portfolioScroll = new ScrollPane(portfolioTable, skin);

        portfolioContainer.add(portfolioScroll).height(3 * TEXT_HEIGHT + 3 * 2).expand().fill().colspan(3);

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(addPortButton).right().width(BTN_WIDTH).expandX();
        portfolioContainer.add(deletePortButton).width(BTN_WIDTH);
        portfolioContainer.add(calcButton).left().width(BTN_WIDTH).expandX();

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(new Label("Initial Date", skin)).right().expandX();
        portfolioContainer.add(initialDateField).left();
        portfolioContainer.add(saveButton).left().width(BTN_WIDTH).expandX();

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(new Label("Initial Balance", skin)).right().expandX();
        portfolioContainer.add(initialBalanceField).left();
        portfolioContainer.add(loadButton).left().width(BTN_WIDTH).expandX();

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(new Label("Monthly Contribution", skin)).right().expandX();
        portfolioContainer.add(monthlyContribField).left().expandX();

        Table stockHdrTable = new Table();
        stockHdrTable.setBackground(background.getDrawable());
        stockHdrTable.pad(1).defaults().expandX().space(4);
        stockHdrTable.row();
        stockHdrTable.add(new Label("Stock", skin));
        stockHdrTable.add(new Label("Percent", skin));
        stockHdrTable.add(new Label("Name", skin));
        stockHdrTable.add(new Label("Current Share Price", skin));
        stockHdrTable.add(new Label("Price Initial Date", skin));
        stockHdrTable.add(new Label("Shares", skin));

        portfolioContainer.row();
        portfolioContainer.add(stockHdrTable).height(TEXT_HEIGHT).expand().fill().colspan(3);

        final Table stockTable = new Table();

        portfolioContainer.row();
        ScrollPane stockScroll = new ScrollPane(stockTable, skin);

        portfolioContainer.add(stockScroll).height(3 * TEXT_HEIGHT).expand().fill().colspan(3);

        TextButton addStockButton = new TextButton("Add Stock", skin);
        addStockButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                stockTable.row();
                TextField tf = new TextField("VFIAX", skin);
                stockTable.add(tf).height(TEXT_HEIGHT).width(150).left();
                stockTable.add(new Label("", skin)).height(TEXT_HEIGHT).width(100).left().expandX();
            }
        });

        TextButton deleteStockButton = new TextButton("Delete Stock", skin);
        deleteStockButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            }
        });

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(addStockButton).right().width(BTN_WIDTH).expandX();
        portfolioContainer.add(deleteStockButton).width(BTN_WIDTH);
        portfolioContainer.add(this.percentSumLabel).left().expandX();

        stage.addActor(portfolioContainer);

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        percentSumLabel.setText("Percent " + percentSum + "%");

        stage.act();
        stage.draw();

        batch.begin();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        //stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public static Texture fillRectangle(int width, int height, Color color, float alpha) {
        Pixmap pix = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pix.setColor(color.r, color.g, color.b, alpha);
        pix.fillRectangle(0, 0, width, height);
        Texture t = new Texture(pix);
        pix.dispose();
        return t;
    }

    private class PortfolioTableRow extends Group {

        TextField name;
        Label value;

        public PortfolioTableRow(String name) {
            this.name = new TextField(name, skin);
            this.value = new Label("value", skin);

            addActor(this.name);
            addActor(this.value);

            this.name.setBounds(getX(), getY(), BTN_WIDTH, TEXT_HEIGHT);
            this.value.setPosition(getX() + BTN_WIDTH + 10, getY());
            this.setBounds(getX(), getY(), BTN_WIDTH * 2, TEXT_HEIGHT);
        }

    }

    private class StockTableRow extends Group {

        TextField stock;
        TextField percent;
        Label name;
        Label currentSharePrice;
        Label priceInitialDate;
        Label shares;
    }

    private class Config {

        String initialDate;
        double initialBalance;
        double monthlyContribution;
        List<PortfolioConfig> portfolioConfig = new ArrayList<>();
    }

    private class PortfolioConfig {

        String portfolioName;
        List<StockConfig> stockConfig = new ArrayList<>();
    }

    private class StockConfig {

        String ticker;
        int percentage;
    }
}
