package ui;

import java.awt.Color;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import data.InvoiceData;

import javax.swing.JSeparator;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class ReportsManagerPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public ReportsManagerPanel() {
		setBackground(new Color(193, 240, 255)); 
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Reports");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(240, 248, 255));
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setVgap(30);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 1;
		add(panel_2, gbc_panel_2);
		
		JLabel todaySales = new JLabel();
		todaySales.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_2.add(todaySales);
		todaySales.setText("<html>Today's Sales:<br><center>" + InvoiceData.todaySales() + " $</center></html>");
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(255, 239, 213));
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setVgap(30);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 2;
		add(panel_3, gbc_panel_3);
		
		JLabel monthlySales = new JLabel();
		monthlySales.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_3.add(monthlySales);
		monthlySales.setText("<html>Monthly Sales:<br><center>" + InvoiceData.monthSales() + " $</center></html>");
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 3;
		add(panel_1, gbc_panel_1);
		
		JFreeChart chartProducts = ChartFactory.createPieChart(
		    "Best Selling Products",
		    computeDataset("products"),
		    true,
		    true,
		    false);
		
		PiePlot plotP = (PiePlot) chartProducts.getPlot();

		plotP.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
		
		JFreeChart chartEmployees = ChartFactory.createPieChart(
			    "Employees Performance",
			    computeDataset("employees"),
			    true,
			    true,
			    false);
		
		PiePlot plotE = (PiePlot) chartProducts.getPlot();

		plotE.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));

		ChartPanel chartPanelProducts = new ChartPanel(chartProducts);
		ChartPanel chartPanelEmployees = new ChartPanel(chartEmployees);
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(chartPanelProducts);
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.add(chartPanelEmployees);
	}
	
	public DefaultPieDataset<String> computeDataset(String type) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		Map<String, Integer> dataCollected = new HashMap<>();
		if (type.equals("products"))
			dataCollected = InvoiceData.getTopFiveProductsSold();
		else {
			if (type.equals("employees"))
				dataCollected = InvoiceData.getUserSalesDistribution();
		}
		for(Map.Entry<String, Integer> p: dataCollected.entrySet()) {
			dataset.setValue(p.getKey(), p.getValue());
		}
		return dataset;
	}
}
