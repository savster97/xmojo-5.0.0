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


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class ItemDialog extends JDialog implements ActionListener,
					KeyListener, ItemListener
{
	//<Begin_Variable_Declarations>
	private boolean initialized = false;
	private java.applet.Applet applet = null;
	private boolean running=false;
	javax.swing.JPanel Top = null;
	javax.swing.JPanel topPanel = null;
	javax.swing.JTextField quantityTF = null;
	javax.swing.JLabel quantityLabel = null;
	javax.swing.JTextField priceTF = null;
	javax.swing.JLabel priceLabel = null;
	javax.swing.JLabel petLabel = null;
	javax.swing.JComboBox categoryCombo = null;
	javax.swing.JLabel invLabel = null;
	javax.swing.JTextField invTF = null;
	javax.swing.JPanel okCancelPanel = null;
	javax.swing.JButton okButton = null;
	javax.swing.JButton cancelButton = null;
	GridBagConstraints cons = new GridBagConstraints();
	Insets inset;
	//<End_Variable_Declarations>
	private boolean isOK = false;


  public ItemDialog()
  {
    //<Begin_ItemDialog>
    pack();

    //<End_ItemDialog>
  }

  public ItemDialog(java.applet.Applet applet)
  {
    //<Begin_ItemDialog_java.applet.Applet>
    this.applet = applet;
    pack();
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    //<End_ItemDialog_java.applet.Applet>
  }


  public ItemDialog(Frame owner) {
	super(owner, true);
	pack();
  }

  public void initInfo(String name, int qnty, Object[] items, boolean enable) {
	isOK = false;
	categoryCombo.removeItemListener(this);
	categoryCombo.removeAllItems();
	if(items != null) {
		for(int i=0; i<items.length; i++) {
			categoryCombo.addItem(items[i]);
		}
	}
	categoryCombo.addItemListener(this);
	categoryCombo.setSelectedItem(name);
	handleChange(name);
	priceTF.setText("$"+Store.getUnitPrice(name));
	priceTF.setOpaque(false);
	quantityTF.setText(qnty+"");
	invTF.setText(Store.getInventoryLevel(name)+"");
	invTF.setOpaque(false);
	categoryCombo.setEnabled(enable);
  }

  public String getName() {
	return (String)categoryCombo.getSelectedItem();
  }

  public int getQuantity() {
	return Integer.parseInt(quantityTF.getText());
  }

  public int getInventory() {
	return Integer.parseInt(invTF.getText());
  }

  public boolean isOK() {
	return this.isOK;
  }

  private void handleChange(String name) {
	invTF.setText(Store.getInventoryLevel(name)+"");
 	priceTF.setText("$"+Store.getUnitPrice(name));
  }

  public void itemStateChanged(ItemEvent e) {
	String sel = getName();
	if(sel == null) {
		return;
	}
	handleChange(sel);
  }

  public void actionPerformed(ActionEvent e) {
	Object src = e.getSource();
	if(src == okButton) {
		try{
			if(getQuantity() > getInventory()) {
				JOptionPane.showMessageDialog(this, 	"We are currently Out of Stock.", "Invalid Input",
						JOptionPane.ERROR_MESSAGE);
				quantityTF.setText(getInventory()+"");
				return;
			}
		}catch(NumberFormatException nfe){
			if(quantityTF.getText().trim().length() == 0){
				JOptionPane.showMessageDialog(this, 	"Please enter the Quantity required", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(this, 	"Please enter a valid Quantity", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			}
			return;
		}
		isOK = true;
	}
	setVisible(false);
  }

  public void keyPressed(KeyEvent e) {
	if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		cancelButton.doClick();
	}
  }

  public void keyReleased(KeyEvent e) {

  }

  public void keyTyped(KeyEvent e) {

  }

  public void setVisible(boolean bl)
  {
	if(false) {
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
	super.setVisible(bl);
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
        this.setSize(getPreferredSize().width+343,getPreferredSize().height+210);
          setTitle("Shopping Item");
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
  }

  /*
  public String getParameter(String input)
  {
           //<Begin_getParameter_String>
           String value = null;
           if ( applet != null)
           {
                 value = applet.getParameter(input);
           }
           else
           {
                 value = (String)com.adventnet.apiutils.Utility.getParameter(input);
           }
           if(value == null)
           {
            }
        return value;


           //<End_getParameter_String>
  }
	*/

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
            topPanel.setBorder(new javax.swing.border.EmptyBorder(5,10,5,10));
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+topPanel,ex);
          }

          try
          {
            quantityLabel.setText("Quantity");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+quantityLabel,ex);
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
            priceLabel.setText("Unit Price");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+priceLabel,ex);
          }

          try
          {
            petLabel.setText("Category");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+petLabel,ex);
          }

          try
          {
            invLabel.setText("Inventory Level");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+invLabel,ex);
          }

          try
          {
            invTF.setEnabled(true);
            invTF.setEditable(false);
            invTF.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            invTF.setText("");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+invTF,ex);
          }

          try
          {
            okButton.setLabel("OK");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+okButton,ex);
          }

          try
          {
            cancelButton.setLabel("Cancel");
          }
          catch(Exception ex)
          {
             showStatus("Exception while setting properties for bean "+cancelButton,ex);
          }
		okButton.setPreferredSize(new Dimension(okButton.getPreferredSize().width+22,okButton.getPreferredSize().height+0));
		okCancelPanel.setPreferredSize(new Dimension(okCancelPanel.getPreferredSize().width+128,okCancelPanel.getPreferredSize().height+0));


          //<End_setUpProperties>
	getRootPane().setDefaultButton(okButton);
  }

  public void initVariables()
  {
        //<Begin_initVariables>
        Top= new javax.swing.JPanel();
        topPanel= new javax.swing.JPanel();
        quantityTF= new javax.swing.JTextField();
        quantityLabel= new javax.swing.JLabel();
        priceTF= new javax.swing.JTextField();
        priceLabel= new javax.swing.JLabel();
        petLabel= new javax.swing.JLabel();
        categoryCombo= new javax.swing.JComboBox();
        invLabel= new javax.swing.JLabel();
        invTF= new javax.swing.JTextField();
        okCancelPanel= new javax.swing.JPanel();
        okButton= new javax.swing.JButton();
        cancelButton= new javax.swing.JButton();


        //<End_initVariables>
  }
  public void setUpGUI(Container container)
  {
//<Begin_setUpGUI_Container>
container.add(Top,BorderLayout.CENTER);
Top.setLayout(new BorderLayout(5,5));
Top.add(topPanel,BorderLayout.CENTER);
topPanel.setLayout(new GridBagLayout());
inset = new Insets(10,10,0,0);
setConstraints(1,1,1,1,0.01,0.0,cons.CENTER,cons.HORIZONTAL,inset,0,0);
topPanel.add(quantityTF,cons);
inset = new Insets(10,0,0,0);
setConstraints(0,1,1,1,0.0050,0.0,cons.WEST,cons.NONE,inset,0,0);
topPanel.add(quantityLabel,cons);
inset = new Insets(10,10,0,0);
setConstraints(1,2,1,1,0.01,0.0,cons.CENTER,cons.HORIZONTAL,inset,0,0);
topPanel.add(priceTF,cons);
inset = new Insets(10,0,0,0);
setConstraints(0,2,1,1,0.0050,0.0,cons.WEST,cons.NONE,inset,0,0);
topPanel.add(priceLabel,cons);
inset = new Insets(0,0,0,0);
setConstraints(0,0,1,1,0.0050,0.0,cons.WEST,cons.NONE,inset,0,0);
topPanel.add(petLabel,cons);
inset = new Insets(0,10,0,0);
setConstraints(1,0,1,1,0.01,0.0,cons.CENTER,cons.HORIZONTAL,inset,0,0);
topPanel.add(categoryCombo,cons);
inset = new Insets(10,0,0,0);
setConstraints(0,3,1,1,0.0050,0.0,cons.WEST,cons.NONE,inset,0,0);
topPanel.add(invLabel,cons);
inset = new Insets(10,10,0,0);
setConstraints(1,3,1,1,0.01,0.0,cons.CENTER,cons.HORIZONTAL,inset,0,0);
topPanel.add(invTF,cons);
Top.add(okCancelPanel,BorderLayout.SOUTH);
okCancelPanel.setLayout(new FlowLayout(1,5,5));
okCancelPanel.add(okButton);
okCancelPanel.add(cancelButton);


//<End_setUpGUI_Container>
  }
  public void setUpConnections()
  {
  //<Begin_setUpConnections>


  //<End_setUpConnections>
	okButton.addActionListener(this);
	cancelButton.addActionListener(this);
	addKeyListener(this);
	categoryCombo.addItemListener(this);
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



  public void setConstraints(int x,int y,int width,int height,double wtX,double wtY,int anchor,int fill,Insets inset,int padX,int padY )
  {
         //<Begin_setConstraints_int_int_int_int_double_double_int_int_Insets_int_int>
	cons.gridx = x;
	cons.gridy = y;
	cons.gridwidth = width;
	cons.gridheight = height;
	cons.weightx = wtX;
	cons.weighty = wtY;
	cons.anchor = anchor;
	cons.fill = fill;
	cons.insets = inset;
	cons.ipadx = padX;
	cons.ipady = padY;


         //<End_setConstraints_int_int_int_int_double_double_int_int_Insets_int_int>
  }
}






