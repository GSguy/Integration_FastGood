package acs.logic;

import java.util.ArrayList;
import java.util.HashMap;

import acs.boundaries.ElementBoundary;
import acs.data.Comment;
import acs.data.Dish;

public class InvokeAction {

	public static void addComment(HashMap<String, Object> actionAttributes, ElementBoundary elementBoundary) {
		if (!actionAttributes.containsKey("header") || !actionAttributes.containsKey("content")) {
			throw new RuntimeException("Missing at least one Comment Attribute ");
		}
		Comment comment = new Comment((String) actionAttributes.get("header"),
				(String) actionAttributes.get("content"));
		Object commentList = elementBoundary.getElementAttributes().get("comments");
		if (commentList == null) {

			ArrayList<Comment> initalizeCommentsList = new ArrayList<>();
			initalizeCommentsList.add(comment);
			HashMap<String, Object> elementAttributesUpdated = (HashMap<String, Object>) elementBoundary
					.getElementAttributes();
			elementAttributesUpdated.put("comments", initalizeCommentsList);
			elementBoundary.setElementAttributes(elementAttributesUpdated);
		} else {
			((ArrayList<Comment>) commentList).add(comment);
		}
	}

	public static void addDish(HashMap<String, Object> actionAttributes, ElementBoundary elementBoundary) {
		if (!actionAttributes.containsKey("name") || !actionAttributes.containsKey("price")) {
			throw new RuntimeException("Missing at least one Dish Attribute  ");
		}
		Object price = actionAttributes.get("price");
		if (price instanceof String) {
			price = Double.valueOf((String) price);
		} else if (price instanceof Integer) {
			price = Double.valueOf((Integer) price);

		}

		Dish dish = new Dish((String) actionAttributes.get("name"), (Double) price);
		Object dishList = elementBoundary.getElementAttributes().get("dishes");

		if (dishList == null) {
			ArrayList<Dish> initalizeDishesList = new ArrayList<>();
			initalizeDishesList.add(dish);
			HashMap<String, Object> elementAttributesUpdated = (HashMap<String, Object>) elementBoundary
					.getElementAttributes();
			elementAttributesUpdated.put("dishes", initalizeDishesList);
			elementBoundary.setElementAttributes(elementAttributesUpdated);
		} else {
			((ArrayList<Dish>) dishList).add(dish);
		}
	}
}
