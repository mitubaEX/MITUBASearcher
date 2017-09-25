import glob
import os
import sys
import csv

csv.field_size_limit(1000000000)
# -*- coding: utf-8 -*-
import codecs
files = []
cvfv = codecs.open("./birth_cvfv.xml","w",'utf-8')
fmc = codecs.open("./birth_fmc.xml","w",'utf-8')
fuc = codecs.open("./birth_fuc.xml","w",'utf-8')
_2gram = codecs.open("./birth_2gram.xml","w",'utf-8')
_3gram = codecs.open("./birth_3gram.xml","w",'utf-8')
_4gram = codecs.open("./birth_4gram.xml","w",'utf-8')
_5gram = codecs.open("./birth_5gram.xml","w",'utf-8')
_6gram = codecs.open("./birth_6gram.xml","w",'utf-8')
smc = codecs.open("./birth_smc.xml","w",'utf-8')
uc = codecs.open("./birth_uc.xml","w",'utf-8')
wsp = codecs.open("./birth_wsp.xml","w",'utf-8')

files = [cvfv, fmc, fuc, _2gram, _3gram, _4gram, _5gram, _6gram, smc, uc, wsp]

def init(filename):
    filename.write("<add>\n")
    filename.write("<doc>\n")

def writer(filename, row):
    if uc == filename:
        filename.write("</doc>\n")
        filename.write("<doc>\n")
        filename.write("<field name=\"filename\">"+unicode(row[0], 'utf-8')+"</field>\n")
        filename.write("<field name=\"place\">"+unicode(row[1], 'utf-8')+"</field>\n")
        filename.write("<field name=\"barthmark\">"+unicode(row[2], 'utf-8')+"</field>\n")
        if len(row[3]) <= 30000:
            filename.write("<field name=\"data\">"+row[3].decode('utf-8').replace('<','&lt;').replace(">",'&gt;').replace("&",'&amp;').replace("\"",'&quot;').replace("\'",'&apo    s;')+"</field>\n")
    else:
        filename.write("</doc>\n")
        filename.write("<doc>\n")
        filename.write("<field name=\"filename\">"+row[0]+"</field>\n")
        filename.write("<field name=\"place\">"+row[1]+"</field>\n")
        filename.write("<field name=\"barthmark\">"+row[2]+"</field>\n")
        if len(row[3]) <= 30000:
            filename.write("<field name=\"data\">"+row[3].decode('utf-8').replace('<','&lt;').replace(">",'&gt;').replace("&",'&amp;').replace("\"",'&quot;').replace("\'",'&apo    s;')+"</field>\n")

def finish_writer(filename):
    filename.write("</doc>\n")
    filename.write("</add>\n")


for j in files:
    init(j)


tmp = glob.glob("./*.csv")
count = 0
for i in tmp:
    reader = open(i).read().split('\n')
    if '\0' not in open(i).read():
        if reader is not None:
            for row in reader:
                row = row.split(',',3)
                if len(row) >= 4:
                    row[0] = row[0].replace('\n',"").replace('<','&lt;').replace(">",'&gt;').replace("&",'&amp;').replace("\"",'&quot;').replace("\'",'&apos;')
                    row[1] = row[1].replace('\n',"").replace('<','&lt;').replace(">",'&gt;').replace("&",'&amp;').replace("\"",'&quot;').replace("\'",'&apos;')
                    row[2] = row[2].replace('\n',"").replace('<','&lt;').replace(">",'&gt;').replace("&",'&amp;').replace("\"",'&quot;').replace("\'",'&apos;')
                    row[3] = row[3].replace('\n',"").replace('<','&lt;').replace(">",'&gt;').replace("&",'&amp;').replace("\"",'&quot;').replace("\'",'&apos;')
                    if "cvfv" in row[2]:
                        writer(cvfv, row)
                    elif "fmc" in row[2]:
                        writer(fmc, row)
                    elif "fuc" in row[2]:
                        writer(fuc, row)
                    elif "2-gram" in str(i):
                        writer(_2gram, row)
                    elif "3-gram" in str(i):
                        writer(_3gram, row)
                    elif "4-gram" in str(i):
                        writer(_4gram, row)
                    elif "5-gram" in str(i):
                        writer(_5gram, row)
                    elif "6-gram" in str(i):
                        writer(_6gram, row)
                    elif "smc" in row[2]:
                        writer(smc, row)
                    elif "uc" in row[2]:
                        writer(uc, row)
                    elif "wsp" in row[2]:
                        writer(wsp, row)

for j in files:
    finish_writer(j)
