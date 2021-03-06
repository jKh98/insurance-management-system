# Insurance Management System
A simple database console application that implements an insurance management system. The system has certain policies such as travel policy, motor policy and medical policy.
Each policy is charactarized by a unique id, an effective date, an expiry date, the policy type, a premium value, valid or not and a unique autogenerated policy number which is a combination of (year-tye-id).

Specific policy types have extra attributes based on their nature. For example, travel policy has departure, destination and a boolean for family. Motor policy has vehicle price as an attribute. Medical 
policy has a list of related beneficiary with at least one associated beneficiary.
A beneficiary type is charactarized by name of the person, their relationship (self, spouse ...), their birth date and gender. A medical policy may have only one beneficiary as self. There are also policy claims that are charactarized by the policy number, the claim date and claim value.

The following java console application builds an SQLite databse based on the described system, adds tables and triggers then inserts and selects test data.

# Requirements
* Requires JRE 8 available [here](https://www.oracle.com/java/technologies/javase-jre8-downloads.html) 
* Add sqlite-jdbc-3.31.1.jar library as a dependency available in ./lib forlder or download [here](https://jar-download.com/artifacts/io.github.willena/sqlite-jdbc/3.31.1/source-code)
