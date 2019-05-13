package com.capgemini.bankapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.capgemini.bankapp.exceptions.AccountNotFoundException;
import com.capgemini.bankapp.exceptions.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;

@WebServlet(urlPatterns =
{ "*.do" }, loadOnStartup = 1)
public class BankAccountController extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private BankAccountService bankService;
	static final Logger LOGGER = Logger.getLogger(BankAccountController.class);

	public BankAccountController()
	{
		bankService = new BankAccountServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		String path = request.getServletPath();

		if (path.equals("/getAllBankAccounts.do"))
		{
			List<BankAccount> bankAccounts = bankService.findAllBankAccount();
			RequestDispatcher dispatcher = request.getRequestDispatcher("display_all_bank_accounts.jsp");
			request.setAttribute("accounts", bankAccounts);
			dispatcher.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		String path = request.getServletPath();
		System.out.println(path);

		if (path.equals("/addNewBankAccount.do"))
		{
			String accountHolderName = request.getParameter("customer_name");
			String accountType = request.getParameter("account_type");
			double balance = Double.parseDouble(request.getParameter("account_balance"));

			BankAccount account = new BankAccount(accountHolderName, accountType, balance);
			if (bankService.addNewBankAccount(account))
			{
				writer.println("<h2>BankAccount is created successfully...");
				writer.println("<h2> <a href='homepage.html'> Home");
				writer.close();
			}
		} else if (path.equals("/withdraw.do"))
		{
			long accountId = Long.parseLong(request.getParameter("account_id"));
			Double amount = Double.parseDouble(request.getParameter("amount"));

			try
			{
				double balance = bankService.withdraw(accountId, amount);
				writer.println("<h2>Withdraw successfully..." + balance);

			} catch (AccountNotFoundException e)
			{
				writer.println("<h2>Account not exists...");
				LOGGER.error("error");
			} catch (LowBalanceException e)
			{
				writer.println("<h2>Insufficient Balance...");
			}
			writer.println("<h2> <a href='homepage.html'> Home");
			writer.close();
		} else if (path.equals("/deposit.do"))
		{
			long accountId = Long.parseLong(request.getParameter("account_id"));
			Double amount = Double.parseDouble(request.getParameter("amount"));

			try
			{
				double balance = bankService.deposit(accountId, amount);
				writer.println("<h2>Deposit successfully..." + balance);

			} catch (AccountNotFoundException e)
			{
				writer.println("<h2>Account not exists...");
			}
			writer.println("<h2> <a href='homepage.html'> Home");
			writer.close();
		} else if (path.equals("/fundtransfer.do"))
		{
			long senderAccountId = Long.parseLong(request.getParameter("sender_account_id"));
			long receiverAccountId = Long.parseLong(request.getParameter("receiver_account_id"));
			Double amount = Double.parseDouble(request.getParameter("amount"));

			try
			{
				double balance = bankService.fundTransfer(senderAccountId, receiverAccountId, amount);
				writer.println("<h2>transfer successfully..." + balance);

			} catch (AccountNotFoundException e)
			{
				writer.println("<h2>Account not exists...");
			} catch (LowBalanceException e)
			{
				writer.println("<h2>Balance not Sufficient to transfer");
			}
			writer.println("<h2> <a href='homepage.html'> Home");
			writer.close();
		} else if (path.equals("/checkbalance.do"))
		{
			long accountId = Long.parseLong(request.getParameter("account_id"));

			try
			{
				double balance = bankService.checkBalance(accountId);
				writer.println("<h2>Balance =  " + balance);
			} catch (AccountNotFoundException e)
			{
				writer.println("Account Not Found");
			}
			writer.println("<h2> <a href='homepage.html'> Home");
			writer.close();
		} else if (path.equals("/deleteaccount.do"))
		{
			long accountId = Long.parseLong(request.getParameter("account_id"));

			try
			{
				boolean result = bankService.deleteBankAccount(accountId);
				if (result)
				{
					writer.println("<h2>Account successfully Deleted ");
				} else
					writer.println("<h2>Failed to delete account");
			} catch (AccountNotFoundException e)
			{
				writer.println("Account Not Found");
			}
			writer.println("<h2> <a href='homepage.html'> Home");
			writer.close();
		} else if (path.equals("/search.do"))
		{
			long accountId = Long.parseLong(request.getParameter("account_id"));
			BankAccount bankAccounts = null;
			RequestDispatcher dispatcher = null;
			try
			{
				bankAccounts = bankService.searchForAccount(accountId);
				dispatcher = request.getRequestDispatcher("searchAccount.jsp");
			} catch (AccountNotFoundException e)
			{
				writer.println("<h2> Account Doesnot exists...");
			}

			request.setAttribute("accounts", bankAccounts);
			dispatcher.forward(request, response);
		} else if (path.equals("/updateSearch.do"))
		{
			long accountId = Long.parseLong(request.getParameter("account_id"));
			BankAccount bankAccounts = null;
			RequestDispatcher dispatcher = null;
			try
			{
				bankAccounts = bankService.searchForAccount(accountId);
				dispatcher = request.getRequestDispatcher("updateMyAccount.jsp");
				request.setAttribute("accounts", bankAccounts);
				dispatcher.forward(request, response);
			} catch (AccountNotFoundException e)
			{
				writer.println("<h2> Account Doesnot exists...");
			}
		}

		else if (path.equals("/update.do"))
		{
			long accountId = Long.parseLong(request.getParameter("account_id"));
			String accountHolderName = request.getParameter("customer_name");
			String accountType = request.getParameter("account_type");
			double balance = Double.parseDouble(request.getParameter("account_balance"));

			BankAccount bankAccounts = new BankAccount(accountId, accountHolderName, accountType, balance);
			if (bankService.updateAccount(bankAccounts))
				response.sendRedirect("getAllBankAccounts.do");
			else
				writer.println("failed to update account");

		}

	}
}
