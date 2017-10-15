package application.view;

import java.util.Arrays;

import application.dialog.LayoutInflater;
import application.fragment.ArticleFragment;
import application.fragment.CategoryFragment;
import application.fragment.DefaultFragment;
import application.fragment.Fragment;
import application.fragment.FragmentTransaction;
import application.fragment.SettingFragment;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MainView implements View {
	private final ImageView rootIcon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("images/blue/tree_root.png")));
	private final ImageView oneIcon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("images/blue/tree_article.png")));
	private final ImageView twoIcon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("images/blue/tree_category.png")));
	private final ImageView threeIcon = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("images/blue/tree_setting.png")));

	private Fragment fragmentDefalt;
	private Fragment fragmentArticle;
	private Fragment fragmentCategory;
	private Fragment fragmentSetting;

	private Fragment lastFragment;

	private FragmentTransaction transaction = new FragmentTransaction();

	private static final int FRAGMENT_ARTICLE = 1;
	private static final int FRAGMENT_CATEGORY = 2;
	private static final int FRAGMENT_SETTING = 3;

	@Override
	public Parent getView() {
			Parent parent = LayoutInflater.inflate("activity_main", Parent.class);
			parent.getStylesheets().add("css/main.css");

			AnchorPane main_left = (AnchorPane) parent.lookup("#main_left");
			StackPane main_center = (StackPane) parent.lookup("#main_center");

			// left
			TreeView treeView = new TreeView();
			// TreeView的内容也是非Node类型，所以不能用SceneBuilder绘图。
		    TreeItem<String> treeItemRoot = new TreeItem<String>("导航菜单",rootIcon);

			TreeItem<String> item_1 = new TreeItem<String>("文章管理",oneIcon);
			TreeItem<String> item_2 = new TreeItem<String>("类别管理",twoIcon);
			TreeItem<String> item_3 = new TreeItem<String>("系统管理",threeIcon);
		    treeItemRoot.getChildren().addAll(Arrays.asList(item_1,item_2,item_3));

		    TreeItem<String> item_3_1 = new TreeItem<String>("设置");
		    TreeItem<String> item_3_2 = new TreeItem<String>("退出");
		    treeItemRoot.getChildren().get(2).getChildren().addAll(Arrays.asList(item_3_1,item_3_2));

		    treeItemRoot.setExpanded(true);

	        treeView.setShowRoot(true);
	        treeView.setRoot(treeItemRoot);
	        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

				@Override
				public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
						TreeItem<String> newValue) {
					Parent container = null;
					switch (newValue.getValue()) {
					case "文章管理":
						setSelection(main_center,FRAGMENT_ARTICLE);
						break;
					case "类别管理":
						setSelection(main_center, FRAGMENT_CATEGORY);
						break;
					case "设置":
						setSelection(main_center, FRAGMENT_SETTING);
						break;
					case "退出":
						Platform.exit();
						break;
					default:
						break;
					}
				}

			});

	        main_left.getChildren().add(treeView);
	        intSelection(main_center);
	        return parent;
	}

	protected void setSelection(StackPane main_center, int layoutId) {
		switch (layoutId) {
		case FRAGMENT_ARTICLE:
			if(fragmentArticle == null){
				fragmentArticle = new ArticleFragment();
			}
			toFragment(main_center,fragmentArticle);
			break;
		case FRAGMENT_CATEGORY:
			if(fragmentCategory == null){
				fragmentCategory = new CategoryFragment();
			}
			toFragment(main_center,fragmentCategory);
			break;
		case FRAGMENT_SETTING:
			if(fragmentSetting == null){
				fragmentSetting = new SettingFragment();
			}
			toFragment(main_center,fragmentSetting);
			break;

		default:
			break;
		}
	}

	private void toFragment(StackPane main_center,Fragment to) {
		if(lastFragment == to) return;
		if(!to.isAdded()){
			transaction.hide(lastFragment).add(main_center,to);
		}else{
			transaction.hide(lastFragment).show(to);
		}
		lastFragment = to;
	}

	private void intSelection(StackPane main_center) {
		//主界面默认的Fragment
		fragmentDefalt = new DefaultFragment();
		transaction.add(main_center, fragmentDefalt);
		lastFragment = fragmentDefalt;
	}
}
