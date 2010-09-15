//#condition polish.usePolishGui
package de.enough.polish.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import de.enough.polish.util.ArrayList;

/**
 * 
 * <p>
 * TabbedPane is a <A HREF="../../../javax/microedition/lcdui/Screen.html" title="class in javax.microedition.lcdui"><CODE>Screen</CODE></A> subclass that presents a series of
 * <A HREF="../../../javax/microedition/lcdui/Screen.html" title="class in javax.microedition.lcdui"><CODE>Screens</CODE></A> to the user and allows them to navigate between them
 * by selecting the corresponding tab.
 * </p>
 * 
 * <p>
 * Each tab has a Screen object that is its <em>contents</em>. Only
 * <A HREF="../../../javax/microedition/lcdui/Form.html" title="class in javax.microedition.lcdui"><CODE>Form</CODE></A> and <A HREF="../../../javax/microedition/lcdui/List.html" title="class in javax.microedition.lcdui"><CODE>List</CODE></A> objects can be added as the contents
 * for a tab; another TabbedPane cannot be used as the contents for a tab.
 * </p>
 * 
 * <p>
 * Each tab must include an icon <A HREF="../../../javax/microedition/lcdui/Image.html" title="class in javax.microedition.lcdui"><CODE>Image</CODE></A> object to indicate its
 * content. The <code>Image</code> may be mutable or immutable; if the
 * <code>Image</code> is mutable, the effect is as if a snapshot of the Image
 * is taken at the time the TabbedPane is constructed or the tab is added to the
 * TabbedPane. Subsequent drawing operations performed on the <code>Image</code>
 * will not automatically appear in the tab. Explicitly calling #setTabIcon with
 * the same Image will cause the icon to be refreshed with the Image's current
 * contents.
 * 
 * <p>
 * Implementations may truncate or scale the icon image if it is larger than the
 * size supported by device. Applications can query the implementation's tab
 * icon size by calling <A HREF="../../../javax/microedition/lcdui/Display.html#getBestImageWidth(int)"><CODE>Display.getBestImageWidth(int)</CODE></A> and
 * <A HREF="../../../javax/microedition/lcdui/Display.html#getBestImageHeight(int)"><CODE>Display.getBestImageHeight(int)</CODE></A> methods using the <A HREF="../../../javax/microedition/lcdui/Display.html#TAB"><CODE>Display.TAB</CODE></A>
 * image type. The style and apperance of tabs are platform-dependent.
 * </p>
 * 
 * <p>
 * The number of tabs is not limited and may exceed the number that can be shown
 * on the screen at one time. In that case, implementations must indicate to
 * users that more tabs are available and provide a mechanism for accessing
 * them. An application can be notified of tab navigation events on a given
 * TabbedPane using the <A HREF="TabListener.html" title="interface in javax.microedition.lcdui"><CODE>TabListener</CODE></A> interface.
 * </P>
 * <img src="doc-files/arrows_in_tab.gif">
 * </P>
 * The TabbedPane's title is shown if one has been provided; otherwise, the
 * title of the selected tab's content is shown instead.
 * </p>
 * <p>
 * Similarly, the TabbedPane's Ticker (if present) takes precedence over the
 * Ticker belonging to the selected tab's contents.
 * </P>
 * <img src="doc-files/tool_tip.gif">
 * 
 * Commands may be added to the TabbedPane and to the contents of its tabs. The
 * Commands shown to the user include those belonging to the TabbedPane and
 * those belonging to the selected tab's contents. A Command is shown only once
 * even it belongs to both objects; however, both object's CommandListeners are
 * notified if it is invoked.
 * </P>
 * 
 * <p>
 * The mechanism used by the user to navigate between tabs is implementation
 * dependent. However, it must allow for the user to navigate within the
 * contents of each tab and between tabs.
 * </p>
 * 
 * <br>
 * TabbedPane implementations on keypad-based deviced SHOULD support vertical
 * and horizontal traversals in the follow manner.<br>
 * 
 * <TABLE BORDER="1" CELLPADDING="1" CELLSPACING="0">
 * <TR BGCOLOR="gray">
 * <TD ALIGN="left" WIDTH="25%">Initial State</TD>
 * <TD ALIGN="left" WIDTH="25%">Down Key press</TD>
 * <TD ALIGN="left" WIDTH="25%">Up key press</TD>
 * <TD ALIGN="left" WIDTH="25%">Left/Right key press</TD>
 * </TR>
 * <TR>
 * <TD ALIGN="left" WIDTH="25%">Focus on tab (tab icon)</TD>
 * <TD ALIGN="left" WIDTH="25%">Focus is set into the tab content (screen
 * object) and its command objects are shown</TD>
 * <TD ALIGN="left" WIDTH="25%">None or platform specific implementation.</TD>
 * <TD ALIGN="left" WIDTH="25%">Change tab focus to next/prior tab. A
 * TabListener.tabChangeEvent is generated.</TD>
 * </TR>
 * <TR>
 * <TD ALIGN="left" WIDTH="25%">Focus into the tab content (Screen object)</TD>
 * <TD ALIGN="left" WIDTH="25%">Default traversal already implemented on both
 * Form and List classes.</TD>
 * <TD ALIGN="left" WIDTH="25%">Focus is set on tab (tab icon) and all command
 * objects from TabbedPane are shown. This behavior supposes that first item is
 * selected on either Form or List objects.</TD>
 * <TD ALIGN="left" WIDTH="25%">Default traversal already implemented on either
 * Form and List classes or specific device Implementation.</TD>
 * </TR>
 * </TABLE>
 * 
 * @since J2ME Polish 2.1 
 */
public class TabbedPane extends Screen
{
	//#if polish.Bugs.needsNokiaUiForSystemAlerts && !polish.SystemAlertNotUsed
		//#define tmp.needsNokiaUiForSystemAlerts
	//#endif
	//#if polish.handleEvents || polish.css.animations
		//#define tmp.handleEvents
	//#endif
	//#if polish.hasCommandKeyEvents || (polish.key.LeftSoftKey:defined && polish.key.RightSoftKey:defined)
		//#define tmp.hasCommandKeyEvents
	//#endif
	//#if polish.useFullScreen
		//#if polish.Screen.base:defined
			//#define tmp.fullScreen
		//#elif (polish.midp2 && !tmp.needsNokiaUiForSystemAlerts) && (!polish.useMenuFullScreen || tmp.hasCommandKeyEvents)
			//#define tmp.fullScreen
		//#elif polish.classes.fullscreen:defined
			//#define tmp.fullScreen
		//#elif polish.build.classes.fullscreen:defined
			//#define tmp.fullScreen
		//#endif
	//#endif
	//#if (polish.useMenuFullScreen && tmp.fullScreen) || polish.needsManualMenu
		//#define tmp.menuFullScreen
		//#if polish.MenuBar.useExtendedMenuBar || polish.classes.MenuBar:defined
			//#define tmp.useExternalMenuBar
		//#endif
	//#endif
	private final ArrayList tabDisplayables;
	private final Container tabIconsContainer;

	private boolean isUseTabTitle;
	private TabListener tabListener;
	private Displayable currentDisplayable;
	private int currentDisplayableIndex = -1;
	private Screen currentScreen;
	private boolean isTabPositionTop;
	private TabbedFormListener tabbedFormListener;
	private boolean isSizeChangedCalled;

	/**
	 * Creates a new, empty TabbedPane, specifying its title.
	 * 
	 * @param title the screen's title (see Displayable).
	 * @see Displayable#setTitle(String)
	 */
	public TabbedPane(String title)
	{
		this( title, null, null, true, null );
	}
	
	/**
	 * Creates a new, empty TabbedPane, specifying its title.
	 * 
	 * @param title the screen's title (see Displayable).
	 * @param style the design
	 * @see Displayable#setTitle(String)
	 */
	public TabbedPane(String title, Style style)
	{
		this( title, null, null, true, style );
	}

	/**
	 * Creates a new TabbedPane, specifying its title and an array of tab
	 * elements and images to be used as its initial content.
	 * 
	 * @param title the screen's title (see Displayable).
	 * @param tabs set of tab elements (Screen objects) which will be part of TabbedPane or null.
	 * @param tabIcons set of images which will be part of each tab element.
	 * @param useTabTitle a boolean value representing the state of the screen's title. true - indicates that title will be set according to tab element's title (current tab), ignoring any title set on the TabbedPane object. false - indicates that title will be fixed, set on the TabbedPane object.
	 * @throws NullPointerException if tabs contains any null elements.
	 * @throws NullPointerException  if tabIcons  contains any null elements.
	 */
	public TabbedPane(String title, Displayable[] tabs, Image[] tabIcons, boolean useTabTitle)
	{
		this( title, tabs, tabIcons, useTabTitle, null );
	}

	/**
	 * Creates a new TabbedPane, specifying its title and an array of tab
	 * elements and images to be used as its initial content.
	 * 
	 * @param title the screen's title (see Displayable).
	 * @param tabs set of tab elements (Screen objects) which will be part of TabbedPane or null.
	 * @param tabIcons set of images which will be part of each tab element.
	 * @param useTabTitle a boolean value representing the state of the screen's title. true - indicates that title will be set according to tab element's title (current tab), ignoring any title set on the TabbedPane object. false - indicates that title will be fixed, set on the TabbedPane object.
	 * @param style the design
	 * @throws NullPointerException if tabs contains any null elements.
	 * @throws NullPointerException if tabIcons  contains any null elements.
	 */
	public TabbedPane(String title, Displayable[] tabs, Image[] tabIcons, boolean useTabTitle, Style style)
	{
		super( title, false, style );
		this.isUseTabTitle = useTabTitle;
		this.tabDisplayables = new ArrayList();
		//#style tabbedpaneicons?
		this.tabIconsContainer = new Container(true);
		this.tabIconsContainer.screen = this;
		if (tabs != null) {
			for (int i = 0; i < tabs.length; i++)
			{
				Displayable screen = tabs[i];
				Image icon = tabIcons[i];
				addTab( screen, icon );
				
			}
		}
	}

	/**
	 * Adds a tab element to the TabbedPane.
	 * 
	 * @param tab screen object (tab element) to be added.
	 * @param icon image part of the tab element.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void addTab( Displayable tab, Image icon)
	{
		addTab( tab, icon, null, null );
	}

	//#if polish.LibraryBuild
		public void addTab( javax.microedition.lcdui.Displayable tab, Image icon) {}
	//#endif

	/**
	 * Adds a tab element to the TabbedPane.
	 * 
	 * @param tab screen object (tab element) to be added.
	 * @param icon image part of the tab element.
	 * @param text the text that should be shown in the tab
	 * @throws NullPointerException if either tab is or icon and text are null.
	 */
	public void addTab( Displayable tab, Image icon, String text)
	{
		addTab( tab, icon, text, null );
	}
	
	//#if polish.LibraryBuild
		public void addTab( javax.microedition.lcdui.Displayable tab, Image icon, String text){}
	//#endif

	
	/**
	 * Adds a tab element to the TabbedPane.
	 * 
	 * @param tab screen object (tab element) to be added.
	 * @param icon image part of the tab element.
	 * @param text the text that should be shown in the tab
	 * @param tabIconStyle the style for tab icon
	 * @throws NullPointerException if either tab is or icon and text are null.
	 */
	public void addTab( Displayable tab, Image icon, String text, Style tabIconStyle)
	{
		if (tab == null || (icon == null && text== null)) {
			throw new NullPointerException();
		}
		IconItem iconItem = new IconItem( text, icon );
		addTab( tab, iconItem, tabIconStyle );
	}
	
	//#if polish.LibraryBuild
		public void addTab( javax.microedition.lcdui.Displayable tab, Image icon, String text, Style tabIconStyle){}
	//#endif

	
	/**
	 * Adds a tab element to the TabbedPane.
	 * 
	 * @param tab screen object (tab element) to be added.
	 * @param iconItem the icon item for the tab
	 * @throws NullPointerException if either tab or iconItem is null.
	 */
	public void addTab( Displayable tab, IconItem iconItem)
	{
		addTab( tab, iconItem, null );
	}
	
	//#if polish.LibraryBuild
		public void addTab( javax.microedition.lcdui.Displayable tab, IconItem iconItem){}
	//#endif

	/**
	 * Adds a tab element to the TabbedPane.
	 * 
	 * @param tab screen object (tab element) to be added.
	 * @param iconItem the icon item for the tab
	 * @param tabIconStyle the style for tab icon
	 * @throws NullPointerException if either tab or iconItem is null.
	 */
	public void addTab( Displayable tab, IconItem iconItem, Style tabIconStyle)
	{
		if (tab == null || iconItem == null) {
			throw new NullPointerException();
		}
		this.tabIconsContainer.add( iconItem, tabIconStyle );
		this.tabDisplayables.add( tab );
		if (this.tabDisplayables.size() == 1 && this.isShown()) {
			// this was the first tab, switch to it by default:
			setFocus(0);
		}
	}
	
	//#if polish.LibraryBuild
		public void addTab( javax.microedition.lcdui.Displayable tab, IconItem iconItem, Style tabIconStyle){}
	//#endif


	/**
	 * Sets a listener for focus change to this TabbedPane, replacing any
	 * previous TabListener. A null reference is allowed and has the effect of
	 * removing any existing listener.
	 * 
	 * @param listener The new listener or null.
	 */
	public void addTabListener( TabListener listener)
	{
		this.tabListener = listener;
	}
	

	/**
	 * Sets a listener for focus change to this TabbedPane, replacing any
	 * previous TabListener. A null reference is allowed and has the effect of
	 * removing any existing listener.
	 * 
	 * @param tabListener The new listener or null.
	 */
	public void setTabListener( TabListener tabListener)
	{
		this.tabListener = tabListener;
	}
	
	/**
	 * Sets the <code>TabbedFormListener</code> to be notified when tab changes happen.
	 * 
	 * @param listener the listener that is notified whenever the user selects another tab,
	 */
	public void setTabbedFormListener( TabbedFormListener listener ) {
		this.tabbedFormListener = listener;
	}
	
	/**
	 * Inserts a tab element into the TabbedPane prior to the element specified.
	 * 
	 * @param index - the index of the tab where insertion is to occur.
	 * @param tab - the tab object (Screen) to be inserted.
	 * @param icon - the image part of the tab element.
	 * @throws IndexOutOfBoundsException - if index is invalid.
	 * @throws NullPointerException - if either tab or icon is null.
	 */
	public void insertTab(int index, Displayable tab, Image icon)
	{
		insertTab( index, tab, icon, null);
	}
	
	//#if polish.LibraryBuild
		public void insertTab(int index, javax.microedition.lcdui.Displayable tab, Image icon){}
	//#endif

	
	/**
	 * Inserts a tab element into the TabbedPane prior to the element specified.
	 * 
	 * @param index the index of the tab where insertion is to occur.
	 * @param tab the tab object (Screen) to be inserted.
	 * @param icon the image part of the tab element.
	 * @param text the text for the tab
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void insertTab(int index, Displayable tab, Image icon, String text)
	{
		insertTab( index, tab, icon, text, null );
	}
	
	//#if polish.LibraryBuild
		public void insertTab(int index, javax.microedition.lcdui.Displayable tab, Image icon, String text){}
	//#endif

	
	/**
	 * Inserts a tab element into the TabbedPane prior to the element specified.
	 * 
	 * @param index the index of the tab where insertion is to occur.
	 * @param tab the tab object (Screen) to be inserted.
	 * @param icon the image part of the tab element.
	 * @param text the text for the tab
	 * @param tabIconStyle the style for the tab icon
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void insertTab(int index, Displayable tab, Image icon, String text, Style tabIconStyle)
	{		
		if (tab == null || (icon == null && text == null)) {
			throw new NullPointerException();
		}
		IconItem iconItem = new IconItem( text, icon );
		insertTab( index, tab, iconItem, tabIconStyle );
	}
	
	//#if polish.LibraryBuild
		public void insertTab(int index, javax.microedition.lcdui.Displayable tab, Image icon, String text, Style tabIconStyle){}
	//#endif

	
	/**
	 * Inserts a tab element into the TabbedPane prior to the element specified.
	 * 
	 * @param index the index of the tab where insertion is to occur.
	 * @param tab the tab object (Screen) to be inserted.
	 * @param tabIconItem the item visualizing the tab
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void insertTab(int index, Displayable tab, IconItem tabIconItem)
	{
		insertTab( index, tab, tabIconItem, null );
	}
	
	//#if polish.LibraryBuild
		public void insertTab(int index, javax.microedition.lcdui.Displayable tab, IconItem tabIconItem){}
	//#endif

	
	/**
	 * Inserts a tab element into the TabbedPane prior to the element specified.
	 * 
	 * @param index the index of the tab where insertion is to occur.
	 * @param tab the tab object (Screen) to be inserted.
	 * @param tabIconItem the item visualizing the tab
	 * @param tabIconStyle the style for the tab icon
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void insertTab(int index, Displayable tab, IconItem tabIconItem, Style tabIconStyle)
	{		
		if (tab == null || tabIconItem == null) {
			throw new NullPointerException();
		}
		this.tabDisplayables.add( index, tab );
		if (tabIconStyle != null) {
			tabIconItem.setStyle( tabIconStyle );
		}
		this.tabIconsContainer.add( index, tabIconItem );
	}
	
	//#if polish.LibraryBuild
		public void insertTab(int index, javax.microedition.lcdui.Displayable tab, IconItem tabIconItem, Style tabIconStyle){}
	//#endif
		
		
	/**
	 * Replaces a tab element at the specified position without changing its icon.
	 * 
	 * @param index the index of the tab which should be replaced
	 * @param tab the tab object (Screen) to be inserted.
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setTab(int index, Displayable tab)
	{
		setTab( index, tab, getTabIconItem(index), null);
	}
	
	//#if polish.LibraryBuild
		public void setTab(int index, javax.microedition.lcdui.Displayable tab){}
	//#endif

		
	/**
	 * Replaces a tab element at the specified position
	 * 
	 * @param index the index of the tab which should be replaced
	 * @param tab the tab object (Screen) to be inserted.
	 * @param icon the image part of the tab element.
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setTab(int index, Displayable tab, Image icon)
	{
		setTab( index, tab, icon, null);
	}
	
	//#if polish.LibraryBuild
		public void setTab(int index, javax.microedition.lcdui.Displayable tab, Image icon){}
	//#endif

	
	/**
	 * Replaces a tab element at the specified position
	 * 
	 * @param index the index of the tab which should be replaced
	 * @param tab the tab object (Screen) to be inserted.
	 * @param icon the image part of the tab element.
	 * @param text the text for the tab
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setTab(int index, Displayable tab, Image icon, String text)
	{
		setTab( index, tab, icon, text, null );
	}
	
	//#if polish.LibraryBuild
		public void setTab(int index, javax.microedition.lcdui.Displayable tab, Image icon, String text){}
	//#endif

	
	/**
	 * Replaces a tab element at the specified position
	 * 
	 * @param index the index of the tab which should be replaced
	 * @param tab the tab object (Screen) to be inserted.
	 * @param icon the image part of the tab element.
	 * @param text the text for the tab
	 * @param tabIconStyle the style for the tab icon
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setTab(int index, Displayable tab, Image icon, String text, Style tabIconStyle)
	{		
		if (tab == null || (icon == null && text == null)) {
			throw new NullPointerException();
		}
		IconItem iconItem = new IconItem( text, icon );
		setTab( index, tab, iconItem, tabIconStyle );
	}
	
	//#if polish.LibraryBuild
		public void setTab(int index, javax.microedition.lcdui.Displayable tab, Image icon, String text, Style tabIconStyle){}
	//#endif
		
	/**
	 * Replaces a tab element at the specified position
	 * 
	 * @param index the index of the tab which should be replaced
	 * @param tab the tab object (Screen) to be inserted.
	 * @param tabIconItem the item visualizing the tab
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setTab(int index, Displayable tab, IconItem tabIconItem)
	{
		setTab( index, tab, tabIconItem, null );
	}
	
	//#if polish.LibraryBuild
		public void setTab(int index, javax.microedition.lcdui.Displayable tab, IconItem tabIconItem){}
	//#endif


	/**
	 * Replaces a tab element into the TabbedPane at specified position
	 * 
	 * @param index the index of the tab which should be replaced
	 * @param tab the tab object (Screen) to be inserted.
	 * @param tabIconItem the item visualizing the tab
	 * @param tabIconStyle the style for the tab icon
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setTab( int index, Displayable tab, IconItem tabIconItem, Style tabIconStyle) {
		//#debug
		System.out.println("setting tab " + tab);
		//try { throw new RuntimeException(); } catch (Exception e) { e.printStackTrace(); }
		if (tab == null || tabIconItem == null) {
			throw new NullPointerException();
		}
		if (this.isShown()) {
			Displayable previous = (Displayable) this.tabDisplayables.get(index);
			if (previous instanceof Canvas) {
				((Canvas) previous).hideNotify();
			}
		}
		this.tabDisplayables.set( index, tab );
		this.tabIconsContainer.set( index, tabIconItem, tabIconStyle );
		if (index == this.currentDisplayableIndex) {
//			Displayable previousDisplayable = this.currentDisplayable; 
//			if (isShown() && previousDisplayable != null && previousDisplayable instanceof Canvas) {
//				((Canvas)previousDisplayable).hideNotify();
//			}
			this.currentDisplayable = null;
			this.currentScreen = null;
			setFocus(index);
		}
	}
	
	//#if polish.LibraryBuild
		public void setTab( int index, javax.microedition.lcdui.Displayable tab, IconItem tabIconItem, Style tabIconStyle){}
	//#endif

	
	/**
	 * Replaces the current tab element without changing its icon.
	 * 
	 * @param tab the tab object (Screen) that should replace the current tab
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setCurrentTab( Displayable tab)
	{
		if (tab == this.currentDisplayable) {
			//#debug 
			System.out.println("ignoring setCurrentTab for " + tab + " as this is the current tab already.");
			repaint();
			return;
		}
		int index = this.currentDisplayableIndex;
		if (index == -1) {
			if (size() == 0) {
				throw new IndexOutOfBoundsException();
			} else {
				index = 0;
			}
		}
		
		setTab( index, tab, getTabIconItem(index), null);
	}
	
	//#if polish.LibraryBuild
		public void setCurrentTab( javax.microedition.lcdui.Displayable tab){}
	//#endif
		
		
	/**
	 * Replaces the current tab element.
	 * 
	 * @param tab the tab object (Screen) that should replace the current tab
	 * @param icon the image part of the tab element.
	 * @throws IndexOutOfBoundsException if there is no tab present
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setCurrentTab( Displayable tab, Image icon)
	{
		setCurrentTab( tab, icon, null);
	}
	
	//#if polish.LibraryBuild
		public void setCurrentTab( javax.microedition.lcdui.Displayable tab, Image icon){}
	//#endif

	
	/**
	 * Replaces the current tab element.
	 * 
	 * @param tab the tab object (Screen) that should replace the current tab
	 * @param icon the image part of the tab element.
	 * @param text the text for the tab
	 * @throws IndexOutOfBoundsException if there is no tab present
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setCurrentTab( Displayable tab, Image icon, String text)
	{
		setCurrentTab( tab, icon, text, null );
	}
	
	//#if polish.LibraryBuild
		public void setCurrentTab( javax.microedition.lcdui.Displayable tab, Image icon, String text){}
	//#endif

	
	/**
	 * Replaces the current tab element.
	 * 
	 * @param tab the tab object (Screen) that should replace the current tab
	 * @param icon the image part of the tab element.
	 * @param text the text for the tab
	 * @param tabIconStyle the style for the tab icon
	 * @throws IndexOutOfBoundsException if there is no tab present
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setCurrentTab( Displayable tab, Image icon, String text, Style tabIconStyle)
	{		
		if (tab == null || (icon == null && text == null)) {
			throw new NullPointerException();
		}
		IconItem iconItem = new IconItem( text, icon );
		setCurrentTab( tab, iconItem, tabIconStyle );
	}
	
	//#if polish.LibraryBuild
		public void setCurrentTab( javax.microedition.lcdui.Displayable tab, Image icon, String text, Style tabIconStyle){}
	//#endif
		
	/**
	 * Replaces the current tab element.
	 * 
	 * @param tab the tab object (Screen) that should replace the current tab
	 * @param tabIconItem the item visualizing the tab
	 * @throws IndexOutOfBoundsException if there is no tab present
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setCurrentTab( Displayable tab, IconItem tabIconItem)
	{
		setCurrentTab( tab, tabIconItem, null );
	}
	
	//#if polish.LibraryBuild
		public void setCurrentTab( javax.microedition.lcdui.Displayable tab, IconItem tabIconItem){}
	//#endif


	/**
	 * Replaces the current tab element.
	 * 
	 * @param tab the tab object (Screen) that should replace the current tab
	 * @param tabIconItem the item visualizing the tab
	 * @param tabIconStyle the style for the tab icon
	 * @throws IndexOutOfBoundsException if there is no tab present
	 * @throws NullPointerException if either tab or icon is null.
	 */
	public void setCurrentTab( Displayable tab, IconItem tabIconItem, Style tabIconStyle) {
		int index = this.currentDisplayableIndex;
		if (index == -1) {
			if (size() == 0) {
				throw new ArrayIndexOutOfBoundsException();
			} else {
				index = 0;
			}
		}
		setTab( index, tab, tabIconItem, tabIconStyle );
	}
	
	//#if polish.LibraryBuild
		public void setCurrentTab( javax.microedition.lcdui.Displayable tab, IconItem tabIconItem, Style tabIconStyle){}
	//#endif
		
	/**
	 * Sets the focus on a tab element.
	 * This method is included for compatibility reasons with TabbedList.
	 * 
	 * @param tabIndex the index of the tab element to receive the focus.
	 * @throws IndexOutOfBoundsException if index is invalid.
	 * @see #setFocus(int)
	 */
	public void setCurrentTab( int tabIndex ) {
		setFocus( tabIndex );
	}

	/**
	 * Sets the focus on a tab element.
	 * 
	 * @param tabIndex the index of the tab element to receive the focus.
	 * @throws IndexOutOfBoundsException if index is invalid.
	 */
	public void setFocus(int tabIndex)
	{
		int previousIndex = this.currentDisplayableIndex;
		Displayable previousDisplayable = this.currentDisplayable;
		if (tabIndex == previousIndex && previousDisplayable != null) {
			// ignore:
			return;
		}
		if (this.tabbedFormListener != null && tabIndex != previousIndex) {
			boolean allowTabChange = this.tabbedFormListener.notifyTabChangeRequested(previousIndex, tabIndex);
			if (!allowTabChange) {
				return;
			}
		}
		boolean isShown = isShown();
		
		if (isShown && previousDisplayable instanceof Canvas) {
			((Canvas)previousDisplayable).hideNotify();
		}
		Displayable disp = (Displayable) this.tabDisplayables.get(tabIndex);
		this.currentDisplayable = disp;
		this.currentDisplayableIndex = tabIndex;
		this.tabIconsContainer.focusChild(tabIndex);
		if (disp instanceof Screen) {
			Screen screen = (Screen) disp;
			if (this.tabListener != null && tabIndex != previousIndex) {
				this.tabListener.tabChangeEvent(screen);
			}
			this.currentScreen = screen;
			int screenFullWidth = getScreenFullWidth();
			int screenFullHeight = getScreenFullHeight();
			if (screenFullWidth != 0 && screenFullHeight != 0) {
				init( screenFullWidth, screenFullHeight);
			}
			if (isShown) {
				screen.showNotify();
			}
		} else  {
			this.currentScreen = null;
			if (isShown && disp instanceof Canvas) {
				((Canvas)disp).showNotify();
			}
		}
		StyleSheet.currentScreen = this;
		if (this.tabbedFormListener != null && tabIndex != previousIndex) {
			this.tabbedFormListener.notifyTabChangeCompleted(previousIndex, tabIndex);
		}
		if (isShown) {
			repaint();
			this.tabIconsContainer.repaint();
		}
	}

	/**
	 * Sets the Image part of the tab element referenced by index, replacing the
	 * previous image of the tab. This method may also be used to update a tab
	 * that was created using a mutable Image; this method should be called with
	 * the same Image after its contents have been changed to update the
	 * appearance of the tab.
	 * 
	 * @param index - the index of the tab element to be set.
	 * @param icon - the new image of the tab element.
	 * @throws IndexOutOfBoundsException - if index is invalid.
	 * @throws NullPointerException - if icon is null.
	 */
	public void setTabIcon(int index, Image icon)
	{
		IconItem tabItem = (IconItem) this.tabIconsContainer.get(index);
		tabItem.setImage( icon );
	}

	/**
	 * Removes a tab element from TabbedPane. If the tab element is being shown
	 * on the display, the implementation should update the display as soon as
	 * it is feasible to do so.
	 * 
	 * @param index the index number of the tab to be removed.
	 * @throws IndexOutOfBoundsException if index is invalid.
	 */
	public void removeTab(int index)
	{
		this.tabDisplayables.remove(index);
		this.tabIconsContainer.remove(index);
		if (index == this.currentDisplayableIndex) {
			this.currentDisplayableIndex = -1;
			if (this.tabDisplayables.size() > index) {
				setFocus( index );
			} else if (this.tabDisplayables.size() > 0) {
				setFocus( this.tabDisplayables.size() - 1 );
			} else {
				//TODO handle case when the only tab is removed
			}
		}
	}

	/**
	 * Gets the number of tab elements in the TabbedPane.
	 * 
	 * @return the number of tab elements in the TabbedPane.
	 */
	public int getCount()
	{
		return this.tabDisplayables.size();
	}
	
	/**
	 * Gets the number of tab elements in the TabbedPane.
	 * 
	 * @return the number of tab elements in the TabbedPane.
	 */
	public int size()
	{
		return this.tabDisplayables.size();
	}

	/**
	 * Returns the index number of a tab element (current tab) in the
	 * TabbedPane.
	 * 
	 * @return index of selected tab, or -1 if none.
	 */
	public int getSelectedIndex()
	{
		return this.currentDisplayableIndex;
	}

	/**
	 * Gets the content for the tab references by the given index.
	 * 
	 * @param index the index number of the tab to be returned.
	 * @return a Screen object (tab element) in the TabbedPane referenced by index, or null if index is invalid or when a Canvas is used.
	 * @see #getDisplayable(int)
	 */
	public Screen getScreen(int index)
	{
		try {
			Displayable screen = (Displayable) this.tabDisplayables.get(index);
			if (screen instanceof Screen) {
				return (Screen) screen;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
		return null;
	}
	
	/**
	 * Gets the content for the tab references by the given index.
	 * 
	 * @param index the index number of the tab to be returned.
	 * @return a Screen object (tab element) in the TabbedPane referenced by index, or null if index is invalid or when a Canvas is used.
	 */
	public Displayable getDisplayable(int index)
	{
		Displayable screen = null;
		try {
			screen = (Displayable) this.tabDisplayables.get(index);
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
		return screen;
	}

	/**
	 * Gets the Image for the tab referenced by the given index.
	 * 
	 * @param index the index number of the tab to be queried.
	 * @return the image part of the tab referenced by index, or null if index is invalid or when the stored tab is not an IconItem.
	 */
	public Image getTabIcon(int index)
	{
		try {
			Item item = this.tabIconsContainer.get(index);
			if (item instanceof IconItem) {
				return ((IconItem)item).getImage();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
		return null;
	}
	
	/**
	 * Gets the text for the tab referenced by the given index.
	 * 
	 * @param index the index number of the tab to be queried.
	 * @return the text part of the tab referenced by index, or null if index is invalid or when the stored tab is not an IconItem.
	 */
	public String getTabText(int index)
	{
		try {
			Item item = this.tabIconsContainer.get(index);
			if (item instanceof StringItem) {
				return ((StringItem)item).getText();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
		return null;
	}
	
	/**
	 * Gets the internal IconItem for the tab referenced by the given index.
	 * 
	 * @param index the index number of the tab to be queried.
	 * @return the icon item of the tab referenced by index, or null if index is invalid or when the stored tab is not an IconItem.
	 */
	public IconItem getTabIconItem(int index)
	{
		try {
			Item item = this.tabIconsContainer.get(index);
			if (item instanceof IconItem) {
				return (IconItem)item;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
		return null;
	}

	//#ifdef polish.useDynamicStyles
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#createCssSelector()
	 */
	protected String createCssSelector()
	{
		return "tabbedpane";
	}
	//#endif

//	/* (non-Javadoc)
//	 * @see de.enough.polish.ui.Screen#paint(Graphics)
//	 */
//	public void paint(Graphics g) {
//		
//		super.paint(g);
//	}
//	
	
	
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#paintBackgroundAndBorder(Graphics)
	 */
	protected void paintBackgroundAndBorder(Graphics g) {
		Screen screen = this.currentScreen;
		if (screen != null) {
			if (!screen.isInitialized) {
				int w = getScreenFullWidth();
				int h = Display.getScreenHeight();
				h -= this.tabIconsContainer.itemHeight;
				screen.init( w, h );
			}
			screen.paintBackgroundAndBorder(g);
		} else {
			super.paintBackgroundAndBorder(g);
		}
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#paintScreen(Graphics)
	 */
	protected void paintScreen(Graphics g) {
		Screen screen = this.currentScreen;
		if (screen != null) {
			screen.paintScreen(g);
		} else {
			super.paintScreen(g);
		}
	}


	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#paintMenuBar(Graphics)
	 */
	protected void paintMenuBar(Graphics g) {
		Container cont = this.tabIconsContainer;
		cont.paint( cont.relativeX, cont.relativeY, cont.relativeX, cont.relativeX + cont.itemWidth, g);
		if (this.currentScreen != null) {
			this.currentScreen.paintMenuBar(g);
		} else {
			super.paintMenuBar(g);
		}
	}
	

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#paintScrollBar(Graphics)
	 */
	protected void paintScrollBar(Graphics g) {
		if (this.currentScreen != null) {
			this.currentScreen.paintScrollBar(g);
		} else {
			super.paintMenuBar(g);
		}
	}


	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#paintTitleAndSubtitle(Graphics)
	 */
	protected void paintTitleAndSubtitle(Graphics g) {
		if (this.isUseTabTitle && this.currentScreen != null) {
			this.currentScreen.paintTitleAndSubtitle(g);
		} else {
			super.paintTitleAndSubtitle(g);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#init(int, int)
	 */
	protected void init(int width, int height) {
		
		if(width == 0 || height == 0) {
			return;
		}
		
		super.init(width, height);
		int tabHeight = this.tabIconsContainer.itemHeight;
		height -= tabHeight;
		if (this.currentScreen != null) {
			this.currentScreen.init(width, height);
			boolean alignWithMenuBar = false;
			//#if tmp.useExternalMenuBar
				MenuBar currentMenuBar = this.currentScreen.getMenuBar();
				if (currentMenuBar.relativeY > height / 2) {
					// menubar is located at bottom:
					alignWithMenuBar = true;
					currentMenuBar.relativeY += tabHeight;
				}
				//System.out.println("setting screen's menubar.relativeY to " + this.currentScreen.getMenuBar().relativeY );
			//#elif tmp.menuFullScreen
				
			//#endif
			if (this.isTabPositionTop) {
				//todo align tabs to the top
			} else {
				//#if tmp.menuFullScreen
					//#if tmp.useExternalMenuBar
						if (alignWithMenuBar) {
							this.tabIconsContainer.relativeY = currentMenuBar.relativeY - tabHeight;
						} else {
							this.tabIconsContainer.relativeY = height;
						}
					//#else
						//TODO set tabIconsContainer.relativeY
					//#endif
				//#else
					this.tabIconsContainer.relativeY = height;
				//#endif
			} 
		}
	}

	/* (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#calculateContentArea(int, int, int, int)
	 */
	protected void calculateContentArea(int x, int y, int width, int height) {
		super.calculateContentArea(x, y, width, height);
		int tabHeight = this.tabIconsContainer.getItemHeight(width, width, height );
		this.contentHeight -= tabHeight;
//		if (this.isTabPositionTop) {
//			this.contentY += tabHeight;
//			this.tabIconsContainer.relativeY = this.contentY;
//		} else {
//			this.tabIconsContainer.relativeY = this.contentHeight;
//		}
	}
	
	

	public void animate(long currentTime, ClippingRegion repaintRegion) {
		super.animate(currentTime, repaintRegion);
		Screen screen = this.currentScreen;
		if (screen != null) {
			screen.animate(currentTime, repaintRegion);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#keyPressed(int)
	 */
	public void keyPressed(int keyCode) {
		if (this.currentScreen != null) {
			this.lastInteractionTime = System.currentTimeMillis();
			this.currentScreen.keyPressed(keyCode);
		} else {
			super.keyPressed(keyCode);	
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#keyRepeated(int)
	 */
	public void keyRepeated(int keyCode) {
		if (this.currentScreen != null) {
			this.currentScreen.keyRepeated(keyCode);
		} else {
			super.keyRepeated(keyCode);	
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#keyReleased(int)
	 */
	public void keyReleased(int keyCode) {
		//#ifdef polish.hasPointerEvents
		if(!Display.getInstance().hasPointerEvents())
		{
		//#endif
			int gameAction = 0;
			try {
				gameAction = getGameAction(keyCode);
			} catch (Exception e) {
				// ignore
			}
			if (gameAction == RIGHT && keyCode != KEY_NUM6 && this.currentDisplayableIndex < this.tabDisplayables.size()-1) {
				setFocus( this.currentDisplayableIndex + 1 );
				return;
			} else if (gameAction == LEFT && keyCode != KEY_NUM4 && this.currentDisplayableIndex > 0) {
				setFocus( this.currentDisplayableIndex - 1 );
				return;
			} else if (gameAction == RIGHT && keyCode != KEY_NUM6 && this.currentDisplayableIndex == this.tabDisplayables.size() - 1 ) {
    			setFocus(0);
    			return;
    		} else if (gameAction == LEFT && keyCode != KEY_NUM4 && this.currentDisplayableIndex == 0 ) {
    			setFocus( this.tabDisplayables.size() - 1 );
    			return;
    		}
		//#ifdef polish.hasPointerEvents
		} 
		//#endif
		if (this.currentScreen != null) {
			this.currentScreen.keyReleased(keyCode);
		} else {
			super.keyReleased(keyCode);	
		}
	}

//	/* (non-Javadoc)
//	 * @see de.enough.polish.ui.Screen#handleKeyReleased(int, int)
//	 */
//	protected boolean handleKeyReleased(int keyCode, int gameAction) {
//		if (this.currentScreen != null) {
//			boolean handled = this.currentScreen.handleKeyReleased(keyCode, gameAction);
//			if(!handled && !Display.getInstance().hasPointerEvents())
//			{
//				if (gameAction == RIGHT && this.currentDisplayableIndex < this.tabDisplayables.size()-1) {
//					setFocus( this.currentDisplayableIndex + 1 );
//				} else if (gameAction == LEFT && this.currentDisplayableIndex > 0) {
//					setFocus( this.currentDisplayableIndex - 1 );
//				}
//				return true;
//			}
//			return handled;
//		} else {
//			return super.handleKeyReleased(keyCode, gameAction);
//		}
//	}
	
	
	//#ifdef polish.hasPointerEvents
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.Canvas#pointerDragged(int, int)
	 */
	public void pointerDragged(int x, int y) {
		Container tabs = this.tabIconsContainer;
		if (tabs.isInItemArea(x - tabs.relativeX, y - tabs.relativeY)) {
			tabs.handlePointerDragged(x - tabs.relativeX, y - tabs.relativeY);
			return;
		}
		if (this.currentScreen != null) {
			this.currentScreen.pointerDragged(x, y);
		} else {
			super.pointerDragged(x, y);
		}
	}
	//#endif

	//#ifdef polish.hasPointerEvents
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.Canvas#pointerDragged(int, int)
	 */
	public void pointerPressed(int x, int y) {
		Container tabs = this.tabIconsContainer;
		if (tabs.isInItemArea(x - tabs.relativeX, y - tabs.relativeY)) {
			if (tabs.handlePointerPressed(x - tabs.relativeX, y - tabs.relativeY)) {
				repaint();
			}
			return;
		}
		if (this.currentScreen != null) {
			this.lastInteractionTime = System.currentTimeMillis();
			this.currentScreen.pointerPressed(x, y);
		} else {
			super.pointerReleased(x, y);
		}
	}
	//#endif

	//#ifdef polish.hasPointerEvents
	/* (non-Javadoc) 
	 * @see javax.microedition.lcdui.Canvas#pointerDragged(int, int)
	 */
	public void pointerReleased(int x, int y) {
		Container tabs = this.tabIconsContainer;
		if (tabs.isInItemArea(x - tabs.relativeX, y - tabs.relativeY)) {
			tabs.handlePointerReleased(x - tabs.relativeX, y - tabs.relativeY);
			int index = tabs.getFocusedIndex();
			if (index != -1) {
				setFocus( index );
			}
			return;
		}
		if (this.currentScreen != null) {
			this.lastInteractionTime = System.currentTimeMillis();
			this.currentScreen.pointerReleased(x, y);
		} else {
			super.pointerReleased(x, y);
		}
	}
	//#endif

	/* (non-Javadoc) 
	 * @see Screen#notifyScreenStateChanged()
	 */
	public void notifyScreenStateChanged() {
		if (this.currentScreen != null) {
			this.currentScreen.notifyScreenStateChanged();
		}
		super.notifyScreenStateChanged();
	}

	/* (non-Javadoc) 
	 * @see Screen#notifyStateListener(Item)
	 */
	public void notifyStateListener(Item item) {
		if (this.currentScreen != null) {
			this.currentScreen.notifyStateListener(item);
		}
		super.notifyStateListener(item);
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#hideNotify()
	 */
	public void hideNotify() {
		if (this.currentScreen != null) {
			this.currentScreen.hideNotify();
		}
		super.hideNotify();
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#showNotify()
	 */
	public void showNotify() {
		if (this.currentDisplayableIndex == -1 && this.tabDisplayables.size() > 0) {
			setFocus(0);
		}
		if (this.currentScreen != null) {
			this.currentScreen.showNotify();
		}
		super.showNotify();
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#sizeChanged(int, int)
	 */
	public void sizeChanged(int width, int height) {
		if (!this.isSizeChangedCalled) {
			this.isSizeChangedCalled = true;
			try {
				this.tabIconsContainer.onScreenSizeChanged(width, height);
				if (this.currentScreen != null) {
					int tabAdjustedHeight = height - this.tabIconsContainer.itemHeight;
					this.currentScreen.sizeChanged(width, tabAdjustedHeight);
				}
				super.sizeChanged(width, height);
			} finally {
				this.isSizeChangedCalled = false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#getCurrentIndex()
	 */
	public int getCurrentIndex() {
		if (this.currentScreen != null) {
			return this.currentScreen.getCurrentIndex();
		}
		return super.getCurrentIndex();
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#getCurrentItem()
	 */
	public Item getCurrentItem() {
		if (this.currentScreen != null) {
			return this.currentScreen.getCurrentItem();
		}
		return super.getCurrentItem();
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#setScrollYOffset(int, boolean)
	 */
	public void setScrollYOffset(int offset, boolean smooth) {
		if (this.currentScreen != null) {
			this.currentScreen.setScrollYOffset(offset, smooth);
		} else {
			super.setScrollYOffset(offset, smooth);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#getScrollHeight()
	 */
	public int getScrollHeight() {
		if (this.currentScreen != null) {
			return this.currentScreen.getScrollHeight();
		} 
		return super.getScrollHeight();
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#getScrollYOffset()
	 */
	public int getScrollYOffset() {
		if (this.currentScreen != null) {
			return this.currentScreen.getScrollYOffset();
		}
		return super.getScrollYOffset();
	}
	
	

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#getScreenContentHeight()
	 */
	public int getScreenContentHeight() {
		if (this.currentScreen != null) {
			return this.currentScreen.getScreenContentHeight();
		}
		return super.getScreenContentHeight();
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#getScreenContentWidth()
	 */
	public int getScreenContentWidth() {
		if (this.currentScreen != null) {
			return this.currentScreen.getScreenContentWidth();
		}
		return super.getScreenContentWidth();
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#getScreenContentX()
	 */
	public int getScreenContentX() {
		if (this.currentScreen != null) {
			return this.currentScreen.getScreenContentX();
		}
		return super.getScreenContentX();
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#getScreenContentY()
	 */
	public int getScreenContentY() {
		if (this.currentScreen != null) {
			return this.currentScreen.getScreenContentY();
		}
		return super.getScreenContentY();
	}

	/*
	 * (non-Javadoc)
	 * @see de.enough.polish.ui.Screen#getRootContainer()
	 */
	public Container getRootContainer() {
		if (this.currentScreen != null) {
			return this.currentScreen.getRootContainer();
		}		
		return super.getRootContainer();
	}
	
	

}
