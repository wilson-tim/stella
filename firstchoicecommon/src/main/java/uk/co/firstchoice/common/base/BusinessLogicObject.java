package uk.co.firstchoice.common.base;

import uk.co.firstchoice.common.base.Transactional.TransactionContext;

/**
 * Generic functionality for business logic objects.
 * 
 *  
 */
public abstract class BusinessLogicObject {

    protected BusinessLogicObject() {
    }

    protected BusinessLogicObject(TransactionContext context) {
        this.setTransactionContext(context);
    }

    protected TransactionContext getTransactionContext() {
        return _transactionContext;
    }

    protected void setTransactionContext(TransactionContext context) {
        if (context == null)
            throw new IllegalArgumentException("Null context not allowed.");
        _transactionContext = context;
    }

    protected TransactionContext _transactionContext = null;
}