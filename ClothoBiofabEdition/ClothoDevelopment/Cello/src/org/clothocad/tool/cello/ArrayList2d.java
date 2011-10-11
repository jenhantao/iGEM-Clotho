/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.cello;

/**
 *
 * @author rozagh
 */
import java.util.ArrayList;

public class ArrayList2d
{
	ArrayList<ArrayList<Integer>>	array;

	public ArrayList2d()
	{
		array = new ArrayList<ArrayList<Integer>>();
	}

	/**
	 * ensures a minimum capacity of num rows. Note that this does not guarantee
	 * that there are that many rows.
	 *
	 * @param num
	 */
	public void ensureCapacity(int num)
	{
		array.ensureCapacity(num);
	}

	/**
	 * Ensures that the given row has at least the given capacity. Note that
	 * this method will also ensure that getNumRows() >= row
	 *
	 * @param row
	 * @param num
	 */
	public void ensureCapacity(int row, int num)
	{
		ensureCapacity(row);
		while (row < getNumRows())
		{
			array.add(new ArrayList<Integer>());
		}
		array.get(row).ensureCapacity(num);
	}

	/**
	 * Adds an item at the end of the specified row. This will guarantee that at least row rows exist.
	 */
	public void Add(Integer data, int row)
	{
		ensureCapacity(row);
		while(row >= getNumRows())
		{
			array.add(new ArrayList<Integer>());
		}
		array.get(row).add(data);
	}

	public Integer get(int row, int col)
	{
		return array.get(row).get(col);
	}

	public void set(int row, int col, Integer data)
	{
		array.get(row).set(col,data);
	}

	public void remove(int row, int col)
	{
		array.get(row).remove(col);
	}

	public boolean contains(Integer data)
	{
		for (int i = 0; i < array.size(); i++)
		{
			if (array.get(i).contains(data))
			{
				return true;
			}
		}
		return false;
	}

	public int getNumRows()
	{
		return array.size();
	}
 
	public int getNumCols(int row)
	{
		return array.get(row).size();
	}
}
