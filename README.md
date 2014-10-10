zewei
=====
bayse的算法来自github，网址： https://github.com/ptnplanet/Java-Naive-Bayes-Classifier  
具体这么使用这可以查看上面的网址，里面写的很清楚。


我主要是将训练和分类进行了分离，从而只需要进行一次训练，就可以进行多次分类。

/src/de/daslaboratorium/machinelearning/bayes/example/路径下的Train类是单独进行训练的类，结果会存入文件中

/src/de/daslaboratorium/machinelearning/bayes/example/路径下的Test类是单独进行分类的类，会将训练的结果从文件中读取，然后进行训练

文件路径可以指定成自己的想要的路径就可以了
