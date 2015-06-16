#查询用户输入法字典
1.查询之前，先插入数据，默认插入的是"insert"
2.查询的时候，在EditText上输出"insert"，下面的ListView展示结果。
3.当EditText为空的时候，会报下面的错误：
Cannot bind argument at index 1 because the index is out of range.  The statement has 0 parameters.
更正已经解决，拼写错误。
4.当查询不到的时候，log输出查询失败
代码提供了CRUD四种操作，这些的目的是熟悉ContentProvider
#创建自己的Content Provider
需要创建Content Provider的几个条件
* You want to offer complex data or files to other applications.
* You want to allow users to copy complex data from your app into other apps.
* You want to provide custom search suggestions using the search framework.
如果不需要这些，使用SQLite代替即可。
接着，使用下面的步骤去创建：
* Design the raw storage for your data. A content provider offers data in two ways:
File data和"Structured" data
* Define a concrete implementation of the ContentProvider class and its required methods.
* Define the provider's authority string, its content URIs, and column names.
* Add other optional pieces, such as sample data or an implementation of AbstractThreadedSyncAdapter that can synchronize data between the provider and cloud-based data.
