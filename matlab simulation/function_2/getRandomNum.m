function node_value_info = getRandomNum( node_info, num_monitor )
%GETRANDOMNUM 此处显示有关此函数的摘要
% function node_value_info = getRandomNum( node_info, num_monitor ):
% generate a node list that monitors has been assigned.
% 
% Input:
%       node_info: the initilized nodes list, num_node rows by 4 cols
%       num_monitor: number of monitors
%   
% Output
%       node_value_info: num_node rows by 4 cols
%       node_value_info(:, 1): the value of node
%       node_value_info(:, 2): the MU of node delay
%       node_value_info(:, 3): the SIGMA of node delay
%       node_value_info(:, 4): the normal value of node
node_value_info = node_info;   % initilize the return

% generate num_monitor rand nums from series 1, 2, ... , k, corresponding to the numbers of nodes
rows_cols_node_info = size(node_info);
num_node = rows_cols_node_info(1, 1);

% the following method is low-efficient when the numbers of monitors is
% large
%{
while 1
    value_node = round(rand(num_monitor, 1) * (num_node - 1) + 1);     % generate a num_monitor by 1 matrix, which will change the first col of node_value_node
                                                % num_node is the range
    temp = unique(value_node);
    if length(temp(:, 1)) == num_monitor             % when the condition that elements in value_nodes are unique is satisfied, get out of while loop
        break;
    end
end
%}

% Compared with function 1, function 2 has changed the priority of these
% nodes.
% Some of nodes has a higher priority, while the others is lower
% relatively.
% In this case, the rule of selecting serial number is different from that
% of function 1.
count = 0;
value_node = zeros(num_monitor, 1); % store the selected serial number
temp_higher1 = [1, 2, 14, 15];
len_higher1 = length(temp_higher1(1, :)); % the length of set containing nodes with higher priority.
temp_higher2 = [3:7];
len_higher2 = length(temp_higher2(1, :));
temp_higher3 = [8:10];
len_higher3 = length(temp_higher3(1, :));
temp_higher4 = [11:13];
len_higher4 = length(temp_higher4(1, :));
temp_lower = [3:7];
len_lower = length(temp_lower(1, :));   % the length of set containing nodes with lower priority.

% if the numbers of monitors is not great than len_higher, 
% selecting nodes should executed in the set of higher priority.
if num_monitor <= len_higher1         
    while count < num_monitor
        indexTemp = round(rand(1, 1) * (len_higher1 - 1 - count) + 1);
        value_node(count + 1, 1) = temp_higher1(1, indexTemp);
        temp_higher1(:, indexTemp) = [];
        count  = count + 1;
    end
elseif num_monitor <= (len_higher2 + len_higher1)
    value_node = setMoniToHiPriNode(value_node, temp_higher1, len_higher1);
    num_monitor_rest = num_monitor - len_higher1;
    while count < num_monitor_rest
        indexTemp = round(rand(1, 1) * (len_higher2 - 1 - count) + 1);
         value_node(len_higher1 + count + 1, 1) = temp_higher2(1, indexTemp);
         temp_higher2(:, indexTemp) = [];
         count = count + 1;
    end
elseif num_monitor <= (len_higher3 + len_higher2 + len_higher1)
    value_node = setMoniToHiPriNode(value_node, [temp_higher1, temp_higher2], len_higher1 + len_higher2);
    num_monitor_rest = num_monitor - (len_higher2 + len_higher1);
    while count < num_monitor_rest
        indexTemp = round(rand(1, 1) * (len_higher3 - 1 - count) + 1);
        value_node(len_higher1 + len_higher2 + count + 1, 1) = temp_higher3(1, indexTemp);
        temp_higher3(:, indexTemp) = [];
        count = count + 1;
    end
else
    value_node = setMoniToHiPriNode(value_node, [temp_higher1, temp_higher2, temp_higher3], len_higher1 + len_higher2 + len_higher3);
    num_monitor_rest = num_monitor - (len_higher3 + len_higher2 + len_higher1);
    while count < num_monitor_rest
        indexTemp = round(rand(1, 1) * (len_higher4 - 1 - count) + 1);
        value_node(len_higher1 + len_higher2 + len_higher3 + count + 1, 1) = temp_higher4(1, indexTemp);
        temp_higher4(:, indexTemp) = [];
        count = count + 1;
    end
end

% if numbers of monitors is greater than len_higher, it means that each
% node from the higher priority set shall be added a monitor. Then, the
% rest of monitors should be add to nodes
% count_low = 0;  % the counter of lower priority set
% if(num_monitor > len_higher1)
%     value_node = setMoniToHiPriNode(value_node, temp_higher1, len_higher1);
%     num_monitor_rest = num_monitor - len_higher1;    % get the numbers of rest monitors
%     while count_low < num_monitor_rest
%         indexTemp_low = round(rand(1, 1) * (len_lower - 1 - count_low) + 1);
%         value_node(len_higher1 + count_low + 1, 1) = temp_lower(1, indexTemp_low);
%         temp_lower(:, indexTemp_low) = [];
%         count_low = count_low + 1;
%     end
% end


if num_monitor > 0      % execute for loops while num_monitor is greater than zero
    for i=1:num_monitor
        index = value_node(i, 1);
        node_value_info(index, 1) = node_info(index, 4);    % set the selected node to its normal
    end
end
end

