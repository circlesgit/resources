#!/usr/bin/perl
#
use strict;

#use warnings;
use IPC::Open3;
use Data::Dumper;
use Getopt::Long qw(GetOptions);

my ($inputfile,         $help,              $filehandle,
    $linenumber,        $userinfo,          $contextinfo,
    $projectinfo,       $resourceinfo,      $permissioninfo,
    @rules,             $command,           $attribute,
    @attributes,        @actions,           $allowaction,
    $denyaction,        $filename,          $subprocess_writer,
    $subprocess_stdout, $subprocess_stderr, $pid,
    $child_exit_status, $inputline,         $acl_output,
    $loopvar,
);

sub usage {
    print " $0 <ARG>
    -i|--inputfile  => input file name,
";
}

GetOptions(
    'inputfile|i=s', => \$inputfile,
    'help|h'         => \$help,
) or die("Errorss in command line arguments\n");

if ( defined $help ) {
    usage();
    exit 0;
}
if ( !defined $inputfile ) {
    print "Mandatory Arguments missing!!!\n";
    usage();
    exit 1;
}
open( $filehandle, "<", "./$inputfile" )
    or die "Could not open $inputfile file : $!\n";
$linenumber = 1;
LINE: while (<$filehandle>) {
    next LINE if /^#/;    # discard comments
    $command   = "rd-acl create ";
    $inputline = $_;
    chomp($inputline);
    ( $userinfo, $contextinfo, $projectinfo, $resourceinfo, $permissioninfo )
        = split( /:/, $inputline );
    $userinfo =~ s/([^-]*)-(.*)/--$1 $2/g;
    $filename = $2;
    $command  = $command . $userinfo . " ";
    $contextinfo =~ s/([^-]*)-(.*)/--$1 $2/g;
    $filename = $filename . "-" . $2;
    $command  = $command . $contextinfo . " ";

    if ( $projectinfo ne undef ) {
        $projectinfo =~ s/([^-]*)-(.*)/--$1 $2/g if defined $projectinfo;
        $filename = $filename . "-" . $2;
        $command  = $command . $projectinfo . " ";
    }
    $filename = "./" . $filename . ".aclpolicy";
    $acl_output->{$filename}->{'filename'} = $filename;
    if ( $resourceinfo =~ m/(([^-]*)-([^,]*)),r(egex)?(.*)/ ) {
        $command = $command . ' --regex ';
        $resourceinfo =~ s/(([^-]*)-([^,]*)),r(egex)?(.*)/$1$5/g;
    }
    if ( $resourceinfo =~ /,/ ) {
        ( $resourceinfo, @attributes ) = split( /,/, $resourceinfo );
        $resourceinfo =~ s/(.*)-(.*)/--$1 $2/g;
        $command   = $command . $resourceinfo . " ";
        $attribute = join( ' --attributes ', @attributes );
        $attribute = ' --attributes ' . $attribute;
        $command   = $command . $attribute;
    }
    else {
        $resourceinfo =~ s/(.*)-(.*)/--$1 $2/g;
        $command = $command . $resourceinfo . " ";
    }
    @actions     = split( /,/, $permissioninfo );
    $allowaction = '';
    $denyaction  = '';
    foreach $loopvar (@actions) {
        if ( $loopvar =~ /^-(.*)$/ ) {
            $denyaction = $denyaction . $1 . ",";
        }
        elsif ( $loopvar =~ /^\+(.*)$/ ) {
            $allowaction = $allowaction . $1 . ",";
        }
        else {
            $allowaction = $allowaction . $loopvar . ",";
        }
    }
    if ( $allowaction ne '' ) {
        $command = $command . " --allow " . $allowaction;
    }
    if ( $denyaction ne '' ) {
        $command = $command . " --deny " . $denyaction;
    }

    #print "cmd formed : $command";
    $pid = open3(
        $subprocess_writer, $subprocess_stdout,
        $subprocess_stderr, $command,
    );
    waitpid( $pid, 0 );
    $child_exit_status = $? >> 8;

    #print "\nexit status $child_exit_status";
    if ( $child_exit_status == 0 ) {
        while (<$subprocess_stdout>) {
            $acl_output->{$filename}->{'output'}
                = $acl_output->{$filename}->{'output'} . $_;
        }
    }
    else {
        print "\nError are line number $linenumber";
        print "\n$inputline\n";
        print "\ncommand : $command";
        while (<$subprocess_stdout>) {
            print "ERROR : $_";
        }
        exit 1;
    }

    close $subprocess_writer;
    close $subprocess_stdout;
    $linenumber++;
}
foreach $loopvar ( keys %$acl_output ) {
    print "\n $acl_output->{$loopvar}->{'filename'}";
    print "\n $acl_output->{$loopvar}->{'output'}";
    open( my $fh, '>', $acl_output->{$loopvar}->{'filename'} )
        or die "Could not open file $acl_output->{$loopvar}->{'filename'} $!";
    print $fh $acl_output->{$loopvar}->{'output'};
    close $fh;
}
close($filehandle);
