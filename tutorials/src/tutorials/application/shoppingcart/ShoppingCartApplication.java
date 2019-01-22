/**
 * The XMOJO Project 5
 * Copyright © 2003 XMOJO.org. All rights reserved.

 * NO WARRANTY

 * BECAUSE THE LIBRARY IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY FOR
 * THE LIBRARY, TO THE EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN
 * OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES
 * PROVIDE THE LIBRARY "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS
 * TO THE QUALITY AND PERFORMANCE OF THE LIBRARY IS WITH YOU. SHOULD THE
 * LIBRARY PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING,
 * REPAIR OR CORRECTION.

 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL
 * ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR REDISTRIBUTE
 * THE LIBRARY AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY
 * GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE
 * USE OR INABILITY TO USE THE LIBRARY (INCLUDING BUT NOT LIMITED TO LOSS OF
 * DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD
 * PARTIES OR A FAILURE OF THE LIBRARY TO OPERATE WITH ANY OTHER SOFTWARE),
 * EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGES.
**/

package tutorials.application.shoppingcart;

import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

import java.io.*;
import java.util.*;

public class ShoppingCartApplication extends JFrame implements ActionListener
{
	//<Begin_Variable_Declarations>
	private boolean initialized = false;
	private java.applet.Applet applet = null;
	private static final String param[] = {};
	private boolean running=false;
	javax.swing.JPanel Top = null;
	javax.swing.JPanel topPanel = null;
	javax.swing.JPanel statsPanel = null;
	javax.swing.JLabel countLabel = null;
	javax.swing.JTextField countTF = null;
	javax.swing.JLabel priceLabel = null;
	javax.swing.JTextField priceTF = null;
	javax.swing.JPanel resetPanel = null;
	javax.swing.JButton resetButton = null;
	javax.swing.JPanel tablePanel = null;
	javax.swing.JScrollPane tableSP = null;
	javax.swing.JTable table = null;
	javax.swing.JPanel buttonPanel = null;
	javax.swing.JButton addButton = null;
	javax.swing.JButton editButton = null;
	javax.swing.JButton delButton = null;
	//<End_Variable_Declarations>
	private ItemDialog itemDialog = null;
	//private MyTableModel model = null;
	private DefaultTableCellRenderer tableRenderer = null;

	public static ShoppingCart scart = new ShoppingCart();

  public ShoppingCartApplication()
  {
    //<Begin_ShoppingCartApplication>
    pack();
	scart.setAppRef(this);
    //<End_ShoppingCartApplication>
  }

  public ShoppingCartApplication(java.applet.Applet applet)
  {
    //<Begin_ShoppingCartApplication_java.applet.Applet>
    this.applet = applet;
    pack();
	scart.setAppRef(this);
    //<End_ShoppingCartApplication_java.applet.Applet>
  }

  public static ShoppingCart getShoppingCartReference() {
	  return scart;
  }

  public static void main(String [] args)
  {
      //<Begin_main_String[]>
      //com.adventnet.apiutils.Utility.parseAndSetParameters(param,args);
     ShoppingCartApplication frame = new ShoppingCartApplication();
     frame.setVisible(true);
     frame.addWindowListener(new WindowAdapter()
     {
       public void windowClosing(WindowEvent evt)
       {
         System.exit(0);
       }
     });
      //<End_main_String[]>
  }


  public void showSummary() {
	countTF.setText(scart.getTotalItemCount()+"");
	priceTF.setText("$"+scart.getTotalPrice());
  }

  public void actionPerformed(ActionEvent e) {
	Object src = e.getSource();
	if(src == addButton) {
		if(table.getRowCount() < 4)
		{
			showItemDialog(Store.PARROT, 1, true);
			if(itemDialog.isOK()) {
				scart.addItem(itemDialog.getName(),
						  new Integer(itemDialog.getQuantity()));
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "All pet categories have already been purchased.\n"+
			"You can select the purchased category and modify its quantity.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
	} else if(src == editButton) {
		ShoppingItem ii = scart.getItemAt(table.getSelectedRow());
		if(ii == null) {
			if(table.getRowCount() == 0){
				JOptionPane.showMessageDialog(this,
					"No Item is available", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(this,
					"Select an Item to edit", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			}
			return;
		}
		showItemDialog(ii.getPetName(), ii.getQuantity().intValue(), false);
		if(itemDialog.isOK()) {
			try{
				scart.updateItem(itemDialog.getName(),
						new Integer(ii.getQuantity().intValue() +
							       itemDialog.getQuantity()));
			}catch(NumberFormatException nfe){
				System.out.println("Enter the Quantity Required");
			}
		}
	} else if(src == delButton) {
		if(table.getSelectedRow() == -1) {
			if(table.getRowCount() == 0){
				JOptionPane.showMessageDialog(this,
					"No Item is available", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(this,
					"Select an Item to delete", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			}
			return;
		}
		scart.removeItemAt(table.getSelectedRow());
	} else if(src == resetButton) {
		scart.clearAll();
	}
	showSummary();
  }

  private void showItemDialog(String name, int qnty, boolean add) {
	if(itemDialog == null) {
		itemDialog = new ItemDialog(this);
		itemDialog.init();
	}
	java.util.List pets = new ArrayList(Store.PET_COUNT);
	if(add) {
		pets.addAll(Store.getItems());
		for(int k=0; k<scart.getRowCount(); k++) {
			ShoppingItem inf = scart.getItemAt(k);
			pets.remove(inf.getPetName());
		}
		if(pets.size() == 0) {
			JOptionPane.showMessageDialog(this, "The pet "+name+" has already been purchased.\n"+
					"You can select the purchased category and modify its quantity.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		name = (String)pets.get(0);
	} else {
		pets.add(name);
	}
	itemDialog.initInfo(name, qnty, pets.toArray(), add);
	itemDialog.setVisible(true);
  }

  public void setVisible(boolean bl)
  {
                 //<Begin_setVisible_boolean>
       	if(bl)
       	{
       	  init();
          start();
        }
        else
        {
          stop();
        }
        super.setVisible(bl);

                 //<End_setVisible_boolean>
  }

    public void stop()
  {
       //<Begin_stop>
       if(!running)
 return;
 running=false;


       //<End_stop>
  }
  public void start()
  {
       //<Begin_start>
       if(running)
 return;
 running=true;


       //<End_start>
  }
  public void init()
  {
        //<Begin_init>
        if (initialized == true) return;
        try
        {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
        this.setSize(getPreferredSize().width+465,getPreferredSize().height+328);
          setTitle("Shopping Cart Application");
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        try
        {
          initVariables();
          setUpGUI(container);
          setUpProperties();
          setUpConnections();
        }
        catch(Exception ex)
        {
          showStatus("Error in init method",ex);
        }
        // let us set the initialzed variable to true so
        // we dont initialize again even if init is called
        initialized = true;


        //<End_init>
	showSummary();
  }

  public void setUpProperties()
  {
  //<Begin_setUpProperties>

          try
          {
            Top.setBorder(new javax.swing.border.EmptyBorder(10,10,10,10));
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+Top,ex);
          }

          try
          {
            countLabel.setText("Item Count :");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+countLabel,ex);
          }

          try
          {
            countTF.setEnabled(true);
            countTF.setEditable(false);
            countTF.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            countTF.setText("");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+countTF,ex);
          }

          try
          {
            priceLabel.setText("Total Price :");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+priceLabel,ex);
          }

          try
          {
            priceTF.setEnabled(true);
            priceTF.setEditable(false);
            priceTF.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            priceTF.setText("");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+priceTF,ex);
          }

          try
          {
            resetButton.setText("Reset");
            resetButton.setToolTipText("Empties the Shopping Cart.");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+resetButton,ex);
          }
 resetButton.setMnemonic('R');

          try
          {
            addButton.setText("Add");
            addButton.setToolTipText("Add an Item in the Shopping Cart.");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+addButton,ex);
          }
 addButton.setMnemonic('A');

          try
          {
            editButton.setText("Edit");
            editButton.setToolTipText("Edit an Item in the Shopping Cart.");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+editButton,ex);
          }
 editButton.setMnemonic('E');

          try
          {
            delButton.setText("Delete");
            delButton.setToolTipText("Remove an Item from the Shopping Cart.");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+delButton,ex);
          }
 delButton.setMnemonic('D');
            		editButton.setPreferredSize(new Dimension(editButton.getPreferredSize().width+14,editButton.getPreferredSize().height+0));
		addButton.setPreferredSize(new Dimension(addButton.getPreferredSize().width+14,addButton.getPreferredSize().height+0));
		tablePanel.setPreferredSize(new Dimension(tablePanel.getPreferredSize().width+10,tablePanel.getPreferredSize().height+10));
		resetPanel.setPreferredSize(new Dimension(resetPanel.getPreferredSize().width+10,resetPanel.getPreferredSize().height+10));
		statsPanel.setPreferredSize(new Dimension(statsPanel.getPreferredSize().width+10,statsPanel.getPreferredSize().height+10));
		topPanel.setPreferredSize(new Dimension(topPanel.getPreferredSize().width+10,topPanel.getPreferredSize().height+10));

          //<End_setUpProperties>
		countTF.setOpaque(false);
		priceTF.setOpaque(false);
  }
  public void initVariables()
  {
    //model = new MyTableModel(scart);

        //<Begin_initVariables>
        Top= new javax.swing.JPanel();
        topPanel= new javax.swing.JPanel();
        statsPanel= new javax.swing.JPanel();
        countLabel= new javax.swing.JLabel();
        countTF= new javax.swing.JTextField();
        priceLabel= new javax.swing.JLabel();
        priceTF= new javax.swing.JTextField();
        resetPanel= new javax.swing.JPanel();
        resetButton= new javax.swing.JButton();
        tablePanel= new javax.swing.JPanel();
        tableSP= new javax.swing.JScrollPane();
        table= new javax.swing.JTable(scart);
        buttonPanel= new javax.swing.JPanel();
        addButton= new javax.swing.JButton();
        editButton= new javax.swing.JButton();
        delButton= new javax.swing.JButton();


        //<End_initVariables>
	tableRenderer = new MyTableCellRenderer();
	table= new javax.swing.JTable(scart) {
		public TableCellRenderer getCellRenderer(int row, int column) {
			return tableRenderer;
		}
	};
	table.getTableHeader().setReorderingAllowed(false);
	table.setRowHeight(80);
  }

  public void setUpGUI(Container container)
  {
//<Begin_setUpGUI_Container>
container.add(Top,BorderLayout.CENTER);
Top.setLayout(new BorderLayout(5,5));
Top.add(topPanel,BorderLayout.NORTH);
topPanel.setLayout(new BorderLayout(5,5));
topPanel.add(statsPanel,BorderLayout.WEST);
statsPanel.setLayout(new GridLayout(2,1,5,5));
statsPanel.add(countLabel);
statsPanel.add(countTF);
statsPanel.add(priceLabel);
statsPanel.add(priceTF);
topPanel.add(resetPanel,BorderLayout.EAST);
resetPanel.setLayout(new BorderLayout(5,5));
resetPanel.add(resetButton,BorderLayout.NORTH);
Top.add(tablePanel,BorderLayout.CENTER);
tablePanel.setLayout(new BorderLayout(5,5));
tablePanel.add(tableSP,BorderLayout.CENTER);
tableSP.getViewport().add(table);
tablePanel.add(buttonPanel,BorderLayout.SOUTH);
buttonPanel.setLayout(new FlowLayout(1,5,5));
buttonPanel.add(addButton);
buttonPanel.add(editButton);
buttonPanel.add(delButton);


//<End_setUpGUI_Container>
  }
  public void setUpConnections()
  {
  //<Begin_setUpConnections>


  //<End_setUpConnections>
	addButton.addActionListener(this);
	editButton.addActionListener(this);
	delButton.addActionListener(this);
	resetButton.addActionListener(this);
  }


  public void showStatus(String message)
  {
     //<Begin_showStatus_String>
     System.out.println("Internal Error :"+ message);
     //<End_showStatus_String>
  }
  public void showStatus(String message,Exception ex)
  {
     //<Begin_showStatus_String_Exception>
     System.out.println("Internal Error :"+ message);
     ex.printStackTrace();
     //<End_showStatus_String_Exception>
  }

	class MyTableCellRenderer extends DefaultTableCellRenderer {

		private Hashtable imageCache = null;

		public MyTableCellRenderer() {
			super();
			imageCache = new Hashtable(Store.PET_COUNT);
		}

		public Component getTableCellRendererComponent(JTable table,
									Object value,
									boolean isSelected,
									boolean hasFocus,
									int row,
									int column) {

			JLabel jl = (JLabel)super.getTableCellRendererComponent(table, value,
									isSelected, hasFocus,
									row, column);
			if(column == 4) {
				String name = (String)scart.getValueAt(row, 0);
				Icon image = getPetIcon(Store.getImageName(name));
				jl.setText(null);
				jl.setIcon(image);
				jl.setHorizontalAlignment(SwingConstants.CENTER);
			} else {
				jl.setIcon(null);
				if(column == 1 || column == 3) {
					jl.setText("$"+String.valueOf(value));
				} else {
					jl.setText(String.valueOf(value));
				}
				jl.setHorizontalAlignment(SwingConstants.LEFT);
			}
			return jl;
		}

		private Icon getPetIcon(String name) {
			if(!imageCache.containsKey(name)) {
				imageCache.put(name, new ImageIcon("images/"+name));
			}
			return (Icon)imageCache.get(name);
		}

 	}

}


