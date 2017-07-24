
// input -> birthmark, csv, csv

extractor = bmsys.extractor(argv[1]);
source1 = fs.open(argv[2]);
source2 = fs.open(argv[3]);
p = extractor.extract(source1);
q = extractor.extract(source2);

// pair2 = bmsys.pairMaker("RoundRobinWithSamePair")
pair2 = bmsys.pairMaker("RoundRobin")
// comparator = bmsys.comparator("JaccardIndex")
// comparator = bmsys.comparator("DiceIndex")
comparator = bmsys.comparator("SimpsonIndex")

birthmarks = p.merge(q);

comparisons = comparator.compare(birthmarks, pair2);

// fs.print(comparisons);
//
// var regExp = new RegExp("/", "g");
var reg = /\//g;
fs.writer("files/" + argv[2].replace(reg,".") + "-" + argv[3].replace(reg,".") + ".csv", comparisons);

// fs.print("extraction: " + birthmarks.time() + " ns")
// fs.print("comparison: " + comparisons.time() + " ns")
// fs.print("" + comparisons.time() + "")
