package xyz.luckin.common.exception.user;

import xyz.luckin.common.exception.BaseException;

/**
 * 用户信息异常类
 * 
 * @author luckin
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
