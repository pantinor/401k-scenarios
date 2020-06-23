package tools.finance;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class GdxGUI extends ApplicationAdapter {

    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private int percentSum = 0;
    private Label percentSumLabel;

    private PortfolioTableRow selectedPortfolio;
    private StockTableRow stockTableIndex = null;

    private static final int TEXT_HEIGHT = 36;
    private static final int BTN_WIDTH = 230;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
    private static final DecimalFormat DF = new DecimalFormat("###,###.##");
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

        percentSumLabel = new Label("", skin);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 1);
        String initialDate = SDF.format(c.getTime());

        TextField initialDateField = new TextField(initialDate, skin);
        TextField initialBalanceField = new TextField("400000", skin);
        TextField monthlyContribField = new TextField("2300", skin);

        Table portfolioContainer = new Table();
        //portfolioContainer.debug();
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

                if (event.getTarget() instanceof PortfolioTableRow) {
                    selectedPortfolio = (PortfolioTableRow) event.getTarget();
                    selectedPortfolio.name.selectAll();
                } else if (event.getTarget().getParent() instanceof PortfolioTableRow) {
                    selectedPortfolio = (PortfolioTableRow) event.getTarget().getParent();
                    selectedPortfolio.name.selectAll();
                }

                stockScroll.setActor(selectedPortfolio.stockTable);
                stockScroll.layout();

            }

            return false;
        });

        TextButton addPortButton = new TextButton("Add Portfolio", skin);
        addPortButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                portfolioTable.row().pad(2);

                PortfolioTableRow row = new PortfolioTableRow("PORTFOLIO");
                portfolioTable.add(row).height(TEXT_HEIGHT).left().expandX();

                stockScroll.setActor(row.stockTable);
                stockScroll.layout();
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
        calcButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                try {
                    Calendar initialDate = Calendar.getInstance();
                    initialDate.setTime(SDF.parse(initialDateField.getText()));

                    double initialBalance = Double.parseDouble(initialBalanceField.getText());
                    double monthlyContrib = Double.parseDouble(monthlyContribField.getText());

                    Iterator<Cell> iter = portfolioTable.getCells().iterator();
                    while (iter.hasNext()) {
                        double currentBalance = 0.0;

                        PortfolioTableRow prow = (PortfolioTableRow) iter.next().getActor();

                        Table smodel = prow.stockTable;
                        for (int j = 0; j < smodel.getRows(); j++) {

                            StockTableRow srow = (StockTableRow) smodel.getCells().get(j).getActor();

                            String ticker = srow.cfg.ticker;
                            int percent = srow.cfg.percentage;

                            double initialBalanceOfThisStock = initialBalance * ((double) percent * 0.01);
                            double monthlyContribBalanceThisStock = monthlyContrib * ((double) percent * 0.01);

                            Stock stock = null;

                            try {
                                stock = YahooFinance.get(ticker, initialDate, Calendar.getInstance(), Interval.MONTHLY);

                                System.out.printf("%s price: %f history: %d\n", ticker, stock.getQuote().getPrice().doubleValue(), stock.getHistory().size());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (stock != null) {
                                BigDecimal price = stock.getQuote().getPrice();
                                BigDecimal fromPrice = stock.getHistory().get(0).getClose();

                                srow.name.setText(stock.getName());
                                srow.currentSharePrice.setText(DF.format(price.doubleValue()) + "");
                                srow.priceInitialDate.setText(DF.format(fromPrice.doubleValue()) + "");

                                double delta = 100 * ((price.doubleValue() - fromPrice.doubleValue()) / fromPrice.doubleValue());

                                if (delta > 20.0) {
                                    srow.currentSharePrice.setColor(Color.GREEN);
                                } else if (delta > 10.0) {
                                    srow.currentSharePrice.setColor(Color.FOREST);
                                } else if (delta > 5.0) {
                                    srow.currentSharePrice.setColor(Color.OLIVE);
                                } else if (delta < -20.0) {
                                    srow.currentSharePrice.setColor(Color.RED);
                                } else if (delta < -10.0) {
                                    srow.currentSharePrice.setColor(Color.FIREBRICK);
                                } else if (delta < -5.0) {
                                    srow.currentSharePrice.setColor(Color.YELLOW);
                                } else {
                                    srow.currentSharePrice.setColor(new Color(1, 1, 1, 1));
                                }

                                double sharesBought = initialBalanceOfThisStock / fromPrice.doubleValue();

                                //skip first quote which is initial
                                for (int k = 1; k < stock.getHistory().size(); k++) {
                                    HistoricalQuote hq = stock.getHistory().get(k);
                                    if (hq.getClose() != null) {
                                        double sharesBoughtThisMonth = monthlyContribBalanceThisStock / hq.getClose().doubleValue();
                                        sharesBought += sharesBoughtThisMonth;
                                    }
                                }

                                currentBalance += sharesBought * stock.getQuote().getPrice().doubleValue();

                                srow.shares.setText(DF.format(sharesBought) + "");

                            }
                        }

                        prow.value.setText(DF.format(currentBalance) + "");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        TextButton saveButton = new TextButton("Save", skin);
        saveButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                try {

                    Config config = new Config();

                    Date initialDate = SDF.parse(initialDateField.getText());

                    config.initialDate = SDF.format(initialDate);
                    config.initialBalance = Double.parseDouble(initialBalanceField.getText());
                    config.monthlyContribution = Double.parseDouble(monthlyContribField.getText());

                    Iterator<Cell> iter = portfolioTable.getCells().iterator();
                    while (iter.hasNext()) {
                        PortfolioTableRow prow = (PortfolioTableRow) iter.next().getActor();

                        config.portfolioConfig.add(prow.cfg);

                        prow.cfg.stockConfig.clear();

                        Table smodel = prow.stockTable;
                        for (int j = 0; j < smodel.getRows(); j++) {
                            StockTableRow srow = (StockTableRow) smodel.getCells().get(j).getActor();
                            prow.cfg.stockConfig.add(srow.cfg);
                        }
                    }

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.setPrettyPrinting().create();
                    String jsons = gson.toJson(config);

                    //Json json = new Json();
                    //String jsons = json.prettyPrint(config);
                    FileHandle file = Gdx.files.local("portfolio.json");
                    file.writeString(jsons, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TextButton loadButton = new TextButton("Load", skin);
        loadButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {

                try {

                    byte[] data = Gdx.files.local("portfolio.json").readBytes();
                    String jsons = new String(data, "UTF-8");

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    Config config = gson.fromJson(jsons, Config.class);

                    //Json json = new Json();
                    //Config config = json.fromJson(Config.class, jsons);
                    initialDateField.setText(config.initialDate);
                    initialBalanceField.setText(config.initialBalance + "");
                    monthlyContribField.setText(config.monthlyContribution + "");

                    portfolioTable.clearChildren();

                    for (PortfolioConfig pcfg : config.portfolioConfig) {

                        portfolioTable.row().pad(2);

                        PortfolioTableRow prow = new PortfolioTableRow(pcfg.portfolioName);
                        portfolioTable.add(prow).height(TEXT_HEIGHT).left().expandX();

                        for (StockConfig scfg : pcfg.stockConfig) {
                            prow.stockTable.row().pad(2);
                            StockTableRow row = new StockTableRow(prow, scfg.ticker, scfg.percentage);
                            prow.stockTable.add(row).height(TEXT_HEIGHT).left().expandX();
                        }

                    }

                    portfolioTable.layout();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        portfolioContainer.add(portfolioHdrTable).height(TEXT_HEIGHT).expand().fill().colspan(3);

        portfolioContainer.row();
        ScrollPane portfolioScroll = new ScrollPane(portfolioTable, skin);

        portfolioContainer.add(portfolioScroll).height(5 * TEXT_HEIGHT + 25).expand().fill().colspan(3);

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(addPortButton).right().width(BTN_WIDTH).expandX();
        portfolioContainer.add(deletePortButton).width(BTN_WIDTH);
        portfolioContainer.add(calcButton).left().width(BTN_WIDTH).expandX();

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(new Label("Initial Date", skin)).right().expandX();
        portfolioContainer.add(initialDateField).left().width(BTN_WIDTH);
        portfolioContainer.add(saveButton).left().width(BTN_WIDTH).expandX();

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(new Label("Initial Balance", skin)).right().expandX();
        portfolioContainer.add(initialBalanceField).left().width(BTN_WIDTH);
        portfolioContainer.add(loadButton).left().width(BTN_WIDTH).expandX();

        portfolioContainer.row().space(5).padBottom(2);
        portfolioContainer.add(new Label("Monthly Contribution", skin)).right().expandX();
        portfolioContainer.add(monthlyContribField).left().width(BTN_WIDTH).expandX();

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
        portfolioContainer.add(stockScroll).height(10 * TEXT_HEIGHT + TEXT_HEIGHT + 8).expand().fill().colspan(3);

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
                percentSum = 0;
                table.getCells().iterator().forEachRemaining(cell -> {

                    try {
                        StockTableRow srow = (StockTableRow) cell.getActor();
                        percentSum += Integer.parseInt(srow.percent.getText());
                    } catch (Exception e) {

                    }
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

        Table stockTable = newStockTable();

        PortfolioConfig cfg;
        TextField name;
        TextField value;

        public PortfolioTableRow(String name) {

            this.cfg = new PortfolioConfig();
            this.cfg.portfolioName = name;

            this.name = new TextField(name, skin);
            this.value = new TextField("value", skin);

            this.name.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    PortfolioTableRow.this.cfg.portfolioName = PortfolioTableRow.this.name.getText();
                }
            });

            addActor(this.name);
            addActor(this.value);

            this.name.setBounds(getX(), getY(), BTN_WIDTH * 2, TEXT_HEIGHT);
            this.value.setBounds(getX() + BTN_WIDTH * 2 + 10, getY(), BTN_WIDTH * 2, TEXT_HEIGHT);

        }

    }

    private class StockTableRow extends Group {

        PortfolioTableRow parent;
        StockConfig cfg;

        TextField stock;
        TextField percent;
        TextField name;
        TextField currentSharePrice;
        TextField priceInitialDate;
        TextField shares;

        public StockTableRow(PortfolioTableRow parent, String stock, int percent) {

            this.parent = parent;

            this.cfg = new StockConfig();
            this.cfg.ticker = stock;
            this.cfg.percentage = percent;

            this.parent.cfg.stockConfig.add(this.cfg);

            this.stock = new TextField(stock, skin);
            this.percent = new TextField("" + percent, skin);

            this.name = new TextField("name of stock", skin);
            this.currentSharePrice = new TextField("1111", skin);
            this.priceInitialDate = new TextField("2222", skin);
            this.shares = new TextField("3333", skin);

            this.name.setDisabled(true);
            this.currentSharePrice.setDisabled(true);
            this.priceInitialDate.setDisabled(true);
            this.shares.setDisabled(true);

            this.stock.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    StockTableRow.this.cfg.ticker = StockTableRow.this.stock.getText();
                }
            });

            this.percent.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    try {
                        StockTableRow.this.cfg.percentage = Integer.parseInt(StockTableRow.this.percent.getText());
                    } catch (Exception e) {

                    }
                }
            });

            addActor(this.stock);
            addActor(this.percent);
            addActor(this.name);
            addActor(this.currentSharePrice);
            addActor(this.priceInitialDate);
            addActor(this.shares);

            int dim = 0;
            this.stock.setBounds(getX(), getY(), 130, TEXT_HEIGHT);
            dim += 130 + 3;
            this.percent.setBounds(getX() + dim, getY(), 60, TEXT_HEIGHT);
            dim += 60 + 3;
            this.name.setBounds(getX() + dim, getY(), 360, TEXT_HEIGHT);
            dim += 360 + 3;
            this.currentSharePrice.setBounds(getX() + dim, getY(), 150, TEXT_HEIGHT);
            dim += 150 + 3;
            this.priceInitialDate.setBounds(getX() + dim, getY(), 150, TEXT_HEIGHT);
            dim += 150 + 3;
            this.shares.setBounds(getX() + dim, getY(), 150, TEXT_HEIGHT);

        }

    }

    private static class Config {

        String initialDate;
        double initialBalance;
        double monthlyContribution;
        List<PortfolioConfig> portfolioConfig = new ArrayList<>();
    }

    private static class PortfolioConfig {

        String portfolioName;
        List<StockConfig> stockConfig = new ArrayList<>();
    }

    private static class StockConfig {

        String ticker;
        int percentage;
    }
}
