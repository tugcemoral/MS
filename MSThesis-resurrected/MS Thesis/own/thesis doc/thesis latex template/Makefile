# Makefile for building thesis and related files if any of the
# SOURCES gets modified.
#
# Serkan Kenar, 2009.

SOURCES = thesis.tex \
	  ch1-introduction/chapter1.tex \
	  ch2/chapter2.tex \
	  ch3/chapter3.tex \
	  ch4/chapter4.tex \
	  ch5-conclusion/chapter5.tex \
	  appendix1/appendix1.tex \
	  appendix2/appendix2.tex \
	  appendix3/appendix3.tex \
	  referanslar.bib

thesis.pdf: thesis.dvi
	dvipdf $^

thesis.dvi: $(SOURCES)
	latex $^
	bibtex thesis
	latex $^
	latex $^

.PHONY: clean
clean:
	rm thesis.dvi
	rm thesis.pdf
