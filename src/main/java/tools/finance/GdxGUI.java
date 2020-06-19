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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
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
    private StockTableRow stockTableIndex = null;

    private static final int TEXT_HEIGHT = 36;
    private static final int BTN_WIDTH = 230;

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

        Container<Table> tableContainer = new Container<>();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.95f;
        float ch = sh * 0.95f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
        tableContainer.fillX();

        Image background = new Image(fillRectangle(300, 10, Color.GRAY, .75f));

        focusIndicator = new Image(fillRectangle(BTN_WIDTH * 1, TEXT_HEIGHT, Color.YELLOW, .35f));
        focusIndicator.setWidth(BTN_WIDTH * 1);
        focusIndicator.setHeight(TEXT_HEIGHT);

        percentSumLabel = new Label("", skin);

        Table portfolioContainer = new Table();
        portfolioContainer.debug();
        portfolioContainer.setFillParent(false);

        Table portfolioHdrTable = new Table();

        portfolioHdrTable.setBackground(background.getDrawable());
        portfolioHdrTable.pad(1).defaults().expandX().space(2);
        portfolioHdrTable.row();
        portfolioHdrTable.add(new Label("Name", skin));
        portfolioHdrTable.add(new Label("Value", skin));

        final Table portfolioTable = new Table();
        final ScrollPane stockScroll = new ScrollPane(newStockTable(), skin);

        portfolioTable.addListener((Event event) -> {
            if (event.toString().equals("touchDown")) {

                if (event.getTarget() == selectedPortfolio || event.getTarget().getParent() == selectedPortfolio) {
                    return false;
                }

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
        });

        TextButton addPortButton = new TextButton("Add Portfolio", skin);
        addPortButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                portfolioTable.row().pad(2);

                PortfolioTableRow row = new PortfolioTableRow("PORTFOLIO");
                portfolioTable.add(row).height(TEXT_HEIGHT).left().expandX();
            }
        });

        TextButton deletePortButton = new TextButton("Delete Portfolio", skin);
        deletePortButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (selectedPortfolio != null) {
                    try {
                        List<Actor> cells = new ArrayList<>();
                        for (Cell c : portfolioTable.getCells().toArray(Cell.class)) {
                            cells.add(c.getActor());
                        }
                        cells.remove(selectedPortfolio);

                        portfolioTable.clearChildren();

                        for (Actor a : cells) {
                            portfolioTable.row().pad(2);
                            portfolioTable.add(a).height(TEXT_HEIGHT).left().expandX();
                        }

                        portfolioTable.layout();

                        Table stockTable = (Table) stockScroll.getActor();
                        stockTable.clearChildren();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    selectedPortfolio = null;
                }
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

        portfolioContainer.add(portfolioScroll).height(5 * TEXT_HEIGHT + 5 * 2).expand().fill().colspan(3);

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
        stockHdrTable.pad(1).defaults().expandX().space(2);
        stockHdrTable.row();
        stockHdrTable.add(new Label("Symbol", skin)).width(120);
        stockHdrTable.add(new Label("%", skin)).width(50);
        stockHdrTable.add(new Label("Name", skin)).width(300);
        stockHdrTable.add(new Label("c-$", skin)).width(100);
        stockHdrTable.add(new Label("i-$", skin)).width(100);
        stockHdrTable.add(new Label("#", skin)).width(100);

        portfolioContainer.row();
        portfolioContainer.add(stockHdrTable).height(TEXT_HEIGHT).expand().fill().colspan(3);

        portfolioContainer.row();
        portfolioContainer.add(stockScroll).height(10 * TEXT_HEIGHT + 10 * 2).expand().fill().colspan(3);

        TextButton addStockButton = new TextButton("Add Stock", skin);
        addStockButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (selectedPortfolio != null) {
                    Table stockTable = (Table) stockScroll.getActor();
                    stockTable.row().pad(2);
                    StockTableRow row = new StockTableRow(selectedPortfolio, "SYM", 0);
                    stockTable.add(row).height(TEXT_HEIGHT).left().expandX();
                }
            }
        });

        TextButton deleteStockButton = new TextButton("Delete Stock", skin);
        deleteStockButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (stockTableIndex != null) {
                    try {
                        Table stockTable = (Table) stockScroll.getActor();
                        List<Actor> cells = new ArrayList<>();
                        for (Cell c : stockTable.getCells().toArray(Cell.class)) {
                            cells.add(c.getActor());
                        }
                        cells.remove(stockTableIndex);
                        stockTableIndex.parent.cfg.stockConfig.remove(stockTableIndex.cfg);

                        stockTable.clearChildren();

                        for (Actor a : cells) {
                            stockTable.row().pad(2);
                            stockTable.add(a).height(TEXT_HEIGHT).left().expandX();
                        }

                        stockTable.layout();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stockTableIndex = null;
                }
            }
        });

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(addStockButton).right().width(BTN_WIDTH).expandX();
        portfolioContainer.add(deleteStockButton).width(BTN_WIDTH);
        portfolioContainer.add(this.percentSumLabel).left().expandX();

        tableContainer.setActor(portfolioContainer);
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);

    }

    private Table newStockTable() {
        Table table = new Table(skin);
        table.addListener((Event event) -> {
            if (event.toString().equals("touchDown")) {
                table.getCells().iterator().forEachRemaining(cell -> {
                    if (event.getTarget().getParent() == cell.getActor()) {
                        GdxGUI.this.stockTableIndex = (StockTableRow) cell.getActor();
                    }
                });
            }
            return false;
        });
        return table;
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
        stage.getViewport().update(width, height, true);
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

        PortfolioConfig cfg;
        TextField name;
        Label value;

        public PortfolioTableRow(String name) {

            this.cfg = new PortfolioConfig();
            this.cfg.portfolioName = name;

            this.name = new TextField(name, skin);
            this.value = new Label("value", skin);

            addActor(this.name);
            addActor(this.value);

            this.name.setBounds(getX(), getY(), BTN_WIDTH, TEXT_HEIGHT);
            this.value.setPosition(getX() + BTN_WIDTH + 10, getY());

        }

    }

    private class StockTableRow extends Group {

        PortfolioTableRow parent;
        StockConfig cfg;
        TextField stock;
        TextField percent;
        Label name;
        Label currentSharePrice;
        Label priceInitialDate;
        Label shares;

        public StockTableRow(PortfolioTableRow parent, String stock, int percent) {

            this.parent = parent;
            
            this.cfg = new StockConfig();
            this.cfg.ticker = stock;
            this.cfg.percentage = percent;
            
            parent.cfg.stockConfig.add(this.cfg);

            this.stock = new TextField(stock, skin);
            this.percent = new TextField("" + percent, skin);
            this.percent.setUserObject(percent);

            this.name = new Label("name of stock", skin);
            this.currentSharePrice = new Label("1111", skin);
            this.priceInitialDate = new Label("2222", skin);
            this.shares = new Label("3333", skin);

            addActor(this.stock);
            addActor(this.percent);
            addActor(this.name);
            addActor(this.currentSharePrice);
            addActor(this.priceInitialDate);
            addActor(this.shares);

            int dim = 0;
            this.stock.setBounds(getX(), getY(), 120, TEXT_HEIGHT);
            dim += 120;
            this.percent.setBounds(getX() + dim + 2, getY(), 50, TEXT_HEIGHT);
            dim += 50;
            this.name.setBounds(getX() + dim + 5, getY(), 300, TEXT_HEIGHT);
            dim += 300;
            this.currentSharePrice.setBounds(getX() + dim + 2, getY(), 100, TEXT_HEIGHT);
            dim += 100;
            this.priceInitialDate.setBounds(getX() + dim + 2, getY(), 100, TEXT_HEIGHT);
            dim += 100;
            this.shares.setBounds(getX() + dim + 2, getY(), 100, TEXT_HEIGHT);

        }

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
