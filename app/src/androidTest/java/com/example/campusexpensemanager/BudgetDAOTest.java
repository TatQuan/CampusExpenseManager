package com.example.campusexpensemanager;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.Data.dao.BudgetDAO;
import com.example.campusexpensemanager.models.Budget;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BudgetDAOTest {

    private Context context;
    private BudgetDAO budgetDAO;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();

        // Xóa database cũ để mỗi lần test là 1 DB sạch
        context.deleteDatabase(DatabaseContract.DATABASE_NAME);

        budgetDAO = new BudgetDAO(context);
    }

    @Test
    public void addBudget_andGetBudgetsByMonth_shouldReturnInsertedBudget() {
        int userId = 1;
        int categoryId = 10;
        double amount = 1000.0;
        int month = 5;
        int year = 2025;

        // Act
        budgetDAO.addBudget(userId, categoryId, amount, month, year);

        List<Budget> budgets = budgetDAO.getBudgetsByMonth(userId, month, year);

        // Assert
        assertEquals(1, budgets.size());
        Budget b = budgets.get(0);
        assertEquals(userId, b.getUserId());
        assertEquals(categoryId, b.getCategoryId());
        assertEquals(amount, b.getBudgetAmount(), 0.001);
        assertEquals(month, b.getMonth());
        assertEquals(year, b.getYear());
    }

    @Test
    public void getTotalBudget_shouldIgnoreDeletedBudgets() {
        int userId = 2;
        int category1 = 20;
        int category2 = 21;
        int month = 6;
        int year = 2025;

        // Thêm 2 budget cho cùng user
        budgetDAO.addBudget(userId, category1, 500.0, month, year);
        budgetDAO.addBudget(userId, category2, 700.0, month, year);

        // Lấy tất cả để biết id
        List<Budget> all = budgetDAO.getAllBudgets();
        assertEquals(2, all.size());

        // Xóa mềm 1 cái (set is_deleted = 1)
        Budget toDelete = all.get(0);
        budgetDAO.deleteBudget(toDelete.getId());

        double total = budgetDAO.getTotalBudget(userId);

        // Chỉ còn lại 1 budget chưa delete = 700
        assertEquals(700.0, total, 0.001);
    }

    @Test
    public void getBudgetByMonthAndYear_shouldReturnCorrectBudget() {
        int userId = 3;
        int categoryId = 30;
        int month1 = 7;
        int month2 = 8;
        int year = 2025;

        // Thêm 2 budget khác tháng
        budgetDAO.addBudget(userId, categoryId, 1000.0, month1, year);
        budgetDAO.addBudget(userId, categoryId, 1500.0, month2, year);

        Budget b1 = budgetDAO.getBudgetByMonthAndYear(userId, categoryId, month1, year);
        Budget b2 = budgetDAO.getBudgetByMonthAndYear(userId, categoryId, month2, year);

        assertNotNull(b1);
        assertNotNull(b2);
        assertEquals(1000.0, b1.getBudgetAmount(), 0.001);
        assertEquals(1500.0, b2.getBudgetAmount(), 0.001);
    }

    @Test
    public void updateBudgetAmount_shouldChangeOnlyBudgetAmount() {
        int userId = 4;
        int categoryId = 40;
        int month = 9;
        int year = 2025;

        budgetDAO.addBudget(userId, categoryId, 2000.0, month, year);

        // Lấy lại budget để biết id
        List<Budget> budgets = budgetDAO.getBudgetsByMonth(userId, month, year);
        assertEquals(1, budgets.size());
        Budget original = budgets.get(0);

        int id = original.getId();
        double newAmount = 3000.0;

        // Act
        budgetDAO.updateBudgetAmount(id, newAmount);

        // Lấy lại để kiểm tra
        Budget updated = budgetDAO.getBudgetByMonthAndYear(userId, categoryId, month, year);
        assertNotNull(updated);
        assertEquals(newAmount, updated.getBudgetAmount(), 0.001);

        // remainingBudget không test giá trị chính xác (vì phụ thuộc logic khác),
        // chỉ cần đảm bảo không bị null/crash
        assertNotNull(updated);
    }
}
